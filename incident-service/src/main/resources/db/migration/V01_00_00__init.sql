-- TABLE
create table esd_incident
(
    id          bigserial primary key,
    created     timestamp    not null,
    modified    timestamp,
    type        varchar(255) not null,
    level       integer      not null,
    status      varchar(255) not null,
    latitude    numeric      not null,
    longitude   numeric      not null,
    title       varchar(255) not null,
    description text         not null
);
