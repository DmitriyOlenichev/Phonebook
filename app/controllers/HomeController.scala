package controllers

import javax.inject.Inject

import anorm.SqlParser.scalar
import models._
import views._
import play.api._
import play.api.mvc._
import views.html
import play.api.data.validation.{Constraint, _}
import play.api.data.Forms._
import play.api.data._
import play.api.i18n._
import play.api.Logger

class HomeController @Inject() (contactService: ContactService, val messagesApi: MessagesApi)
  extends Controller with I18nSupport {

  val Home = Redirect(routes.HomeController.list(""))

  val phoneUniqueConstraint: Constraint[String] = Constraint("constraints.uniquePhone")({
    plainText =>
      val errors = plainText match {
        case phone if !contactService.isPhoneFree(phone) => Seq(ValidationError(Messages("phoneUniqueError")))
        case _ => Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })

  val contactForm = Form(
    mapping(   //tuple
      "id" -> ignored(None:Option[Long]),
      "name" -> nonEmptyText,
      "phone" -> nonEmptyText.verifying(Constraints.pattern(contactService.phoneFormat), phoneUniqueConstraint) //
      //"phone" -> nonEmptyText.verifying(Constraints.pattern("""^((8|\+7))?(\(?\d{3}\)?)?[\d\- ]{5,14}$""".r, name = "constraint.pattern", error = "error.patt"), phoneUniqueConstraint) //
    ) (Contact.apply)(Contact.unapply)
  )

  def index = Action { Home }

  /**
    * Display the filtered list of contacts
    */
  def list(filter: String) = Action { implicit request =>
    Ok(html.index(
      contactService.list(filter = ("%" + filter + "%")), filter
    ))

  }

  /**
    * Display the 'new contact form'
    */
  def create = Action { implicit request =>
    Ok(html.createForm(contactForm))
  }

  /**
    * Handle the 'new contact form' submission
    */
  def save = Action { implicit request =>
    contactForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.createForm(formWithErrors)),  //, contactService.options
      contact => {
        contactService.insert(contact)
        Home.flashing("success" -> Messages("phone-book.created", contact.name))
      }
    )
  }

  /**
    * delete the single contact
    */
  def delete(id: Long) = Action {
    contactService.delete(id)
    Home.flashing("success" -> Messages("phone-book.deleted"))
  }

  /*
   * count contacts by filter
   */
  def count(filter: String) = Action { implicit request =>
    Ok(contactService.count(filter = "%" + filter + "%").toString())
  }
}