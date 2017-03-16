# --- First database schema

# --- !Ups

set ignorecase true;

create table phone_book (
  id bigint not null auto_increment,
  name varchar(255) not null,
  phone varchar(20) not null,
  constraint pk_phone_book primary key (id)
);

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists phone_book;

SET REFERENTIAL_INTEGRITY TRUE;



