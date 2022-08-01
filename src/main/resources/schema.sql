Create TABLE Todo
(
    id integer not null,
    USER_NAME varchar(50) not null,
    description varchar(255) not null,
    date_of_completion DATE,
    is_completed BOOLEAN,
    primary key(id)
);