create table link_conversion
(
    id        bigserial not null,
    url       varchar   not null,
    deep_link varchar   not null,
    created   timestamp not null
);

create
unique index link_conversion_id_uindex
    on link_conversion (id);

alter table link_conversion
    add constraint link_conversion_pk
        primary key (id);
