-- Migração para suportar a nova modelagem: bicos.cidade_id passando a referenciar cidades.id
-- Esta migração é segura tanto para DBs existentes quanto para DBs novos.

ALTER TABLE IF EXISTS bicos
    ADD COLUMN IF NOT EXISTS cidade_id BIGINT;

CREATE TABLE IF NOT EXISTS cidades (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Insere cidades a partir de valores antigos em bicos.city, se essa coluna existir.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'bicos'
          AND column_name = 'city'
    ) THEN
        INSERT INTO cidades (name)
        SELECT DISTINCT city
        FROM bicos
        WHERE city IS NOT NULL
        ON CONFLICT (name) DO NOTHING;

        UPDATE bicos
        SET cidade_id = c.id
        FROM cidades c
        WHERE lower(c.name) = lower(bicos.city);

        ALTER TABLE bicos DROP COLUMN city;
    END IF;
END$$;

-- O constrangimento de FK já é definido no schema inicial, então não é necessário recriá-lo aqui.
