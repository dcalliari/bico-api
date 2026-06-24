ALTER TABLE users
    ADD COLUMN full_name VARCHAR(255),
    ADD COLUMN cpf VARCHAR(255);

UPDATE users SET full_name = login WHERE full_name IS NULL;

UPDATE users
SET cpf = sub.cpf
FROM (
    SELECT id,
           concat('9', lpad(row_number() OVER (ORDER BY id)::text, 10, '0')) AS cpf
    FROM users
    WHERE cpf IS NULL
) sub
WHERE users.id = sub.id;

ALTER TABLE users
    ALTER COLUMN full_name SET NOT NULL,
    ALTER COLUMN cpf SET NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT users_cpf_unique UNIQUE (cpf);