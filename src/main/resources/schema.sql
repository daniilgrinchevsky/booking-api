drop table if exists booking;
create table booking
(
    id      bigint    not null auto_increment primary key,
    date    date      not null,
    film_id bigint    not null,
    seat    integer   not null
);
