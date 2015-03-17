# --- !Ups

create table NFR (
    id bigint not null,
    desc varchar(255) not null,
    primary key (id)
);

create sequence entity_seq;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists NFR;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence entity_seq;
