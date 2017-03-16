package models

import javax.inject.Inject

import play.api.db._
import anorm._
import anorm.SqlParser._
import play.api.Logger

case class Contact(id: Option[Long], name: String, phone: String)

@javax.inject.Singleton
class ContactService @Inject() (dbApi: DBApi) {

  private val db = dbApi.database("default")

  val phoneFormat = """^(\+)?\d{5,15}$""".r

  val contact = {
    get[Option[Long]]("id") ~
      get[String]("name") ~
      get[String]("phone") map {
      case id~name~phone => Contact(id, name, phone)
    }
  }

  def isPhoneFree(phone: String): Boolean = findByPhone(phone).isEmpty

  def findById(id: Long): Option[Contact] = {
    db.withConnection { implicit connection =>
      SQL("select * from phone_book where id = {id}").on('id -> id).as(contact.singleOpt)
    }
  }

  def findByPhone(phone: String): Option[Contact] = {
    db.withConnection { implicit connection =>
      SQL("select * from phone_book where phone = {phone}").on('phone -> phone).as(contact.singleOpt)
    }
  }

  /**
    * Return filtered list of contacts
    */
  def list(filter: String = "%"): List[Contact] = {
    db.withConnection { implicit connection =>
      SQL(
        """
          select * from phone_book
          where phone_book.name like {filter}
      """
      ).on(
        'filter -> filter
      ).as(contact *)
    }
  }

  /**
    * Return contacts count by filter
    */
  def count(filter: String = "%"): Long = {
    db.withConnection { implicit connection =>
      SQL(
        """
          select count(*) from phone_book
          where phone_book.name like {filter}
        """
      ).on(
        'filter -> filter
      ).as(scalar[Long].single)
    }
  }

  /**
    * Insert a new contact
    */
  def insert(contact: Contact) = {
    db.withConnection { implicit connection =>
      SQL(
        """
          insert into phone_book(name, phone) values (
            {name}, {phone}
          )
        """
      ).on(
        'name -> contact.name,
        'phone -> contact.phone
      ).executeUpdate()
    }
  }

  /**
    * Delete a contact
    */
  def delete(id: Long) = {
    db.withConnection { implicit connection =>
      SQL("delete from phone_book where id = {id}").on('id -> id).executeUpdate()
    }
  }
}

