# --- !Ups

create table nfr (
    id bigint not null,
    description varchar(255) not null,
    primary key (id)
);

create sequence entity_seq;

# --- !Downs

drop table if exists nfr;

drop sequence entity_seq;
