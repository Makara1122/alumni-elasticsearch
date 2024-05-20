CREATE TABLE students (
                          student_id SERIAL PRIMARY KEY,
                          name VARCHAR(255),
                          title VARCHAR(255),
                          skills TEXT[],
                          deleted BOOLEAN DEFAULT FALSE  -- Adding a 'deleted' column with a default value of FALSE
);

INSERT INTO public.students (student_id, name, title, skills, deleted)
VALUES (747, 'David Anderson', 'Machine Learning Engineer', ARRAY['Python', 'TensorFlow', 'Keras'], FALSE);


INSERT INTO students (student_id, name, title, skills, deleted)
VALUES (10, 'Makara Is try so hard', 'Machine Learning Engineer', ARRAY['Python', 'TensorFlow', 'Keras'], FALSE);


-- auto-generated definition
create table author_tbl2
(
    id                     varchar(255) not null
        primary key,

    email                  varchar(255),
    is_account_expired     boolean      not null,
    is_account_locked      boolean      not null,
    is_credentials_expired boolean      not null,
    is_disabled            boolean      not null
    ,
    name                   varchar(255),
    password               varchar(255)
);




INSERT INTO public.author_tbl2 (id, email, is_account_expired, is_account_locked, is_credentials_expired,
                               is_disabled, name, password)
VALUES (123344, 'makara@gmali.com', false, false, false, false,  'makara', 'hsdjls63738');
