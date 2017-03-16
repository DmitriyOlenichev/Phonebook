# --- Sample dataset

# --- !Ups

insert into phone_book (id,name,phone) values (1, 'Dmitriy', '+73439439453');
insert into phone_book (id,name,phone) values (2, 'Den', '+167657568766');
insert into phone_book (id,name,phone) values (3, 'Dina', '543534563446');
insert into phone_book (id,name,phone) values (4, 'frt', '1111111');
insert into phone_book (name,phone) values ('tret', '11144121');
insert into phone_book (name,phone) values ('gg', '4324353');

# --- !Downs

delete from phone_book;
