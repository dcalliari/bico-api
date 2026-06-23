-- Dados iniciais para suportar ambientes de desenvolvimento e testes com Flyway

INSERT INTO cidades (id, name) VALUES
(1, 'Belém'),
(2, 'Ananindeua'),
(3, 'Marituba')
ON CONFLICT (id) DO NOTHING;

INSERT INTO users (
    id, login, mail, password, role, created_at, updated_at
) VALUES
('11111111-1111-1111-1111-111111111111', 'admin', 'admin@bicos.com',
 '$2a$10$aXKqAUIKoUIW6pKWY5q53OqAGVr2O/FJlYMLWTczSINKlRv7N7Il6',
 'ADMIN', NOW(), NOW()),

('22222222-2222-2222-2222-222222222222', 'aprovador.n1', 'n1@bicos.com',
 '$2a$10$aXKqAUIKoUIW6pKWY5q53OqAGVr2O/FJlYMLWTczSINKlRv7N7Il6',
 'APROVADOR_N1', NOW(), NOW()),

('33333333-3333-3333-3333-333333333333', 'aprovador.n2', 'n2@bicos.com',
 '$2a$10$aXKqAUIKoUIW6pKWY5q53OqAGVr2O/FJlYMLWTczSINKlRv7N7Il6',
 'APROVADOR_N2', NOW(), NOW()),

('44444444-4444-4444-4444-444444444444', 'aprovador.n3', 'n3@bicos.com',
 '$2a$10$aXKqAUIKoUIW6pKWY5q53OqAGVr2O/FJlYMLWTczSINKlRv7N7Il6',
 'APROVADOR_N3', NOW(), NOW()),

('55555555-5555-5555-5555-555555555555', 'joao', 'joao@email.com',
 '$2a$10$aXKqAUIKoUIW6pKWY5q53OqAGVr2O/FJlYMLWTczSINKlRv7N7Il6',
 'FREELANCER', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO bicos (
    id,
    user_id,
    name,
    description,
    cidade_id,
    price,
    bicos_filter,
    data_hora_servico,
    created_at,
    updated_at
) VALUES

(1, '55555555-5555-5555-5555-555555555555',
 'Faxina residencial',
 'Limpeza completa de apartamento',
 1, 180.00, 'FAXINA',
 '2026-07-01 08:00:00', NOW(), NOW()),

(2, '55555555-5555-5555-5555-555555555555',
 'Entrega de documentos',
 'Entrega urgente em Belém',
 1, 80.00, 'ENTREGAS',
 '2026-07-02 09:00:00', NOW(), NOW()),

(3, '55555555-5555-5555-5555-555555555555',
 'Manutenção elétrica',
 'Troca de tomadas e interruptores',
 2, 250.00, 'MANUTENCAO',
 '2026-07-05 14:00:00', NOW(), NOW()),

(4, '55555555-5555-5555-5555-555555555555',
 'Suporte de TI',
 'Configuração de rede doméstica',
 1, 300.00, 'TI',
 '2026-07-07 10:00:00', NOW(), NOW()),

(5, '55555555-5555-5555-5555-555555555555',
 'Garçom para evento',
 'Evento corporativo',
 3, 220.00, 'EVENTOS',
 '2026-07-10 18:00:00', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO candidatura (
    id,
    user_id,
    bicos_id,
    status,
    data_solicitacao
) VALUES

(1, '22222222-2222-2222-2222-222222222222', 1, 'PENDENTE', NOW()),
(2, '33333333-3333-3333-3333-333333333333', 2, 'AGUARDANDO_N2', NOW()),
(3, '44444444-4444-4444-4444-444444444444', 3, 'AGUARDANDO_N3', NOW()),
(4, '55555555-5555-5555-5555-555555555555', 4, 'APROVADO', NOW()),
(5, '22222222-2222-2222-2222-222222222222', 5, 'REJEITADO', NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO hist_aprov (
    id,
    candidatura_id,
    user_id,
    decisao,
    motivo,
    data_aprovacao
) VALUES

(1, 4, '44444444-4444-4444-4444-444444444444',
 'APROVADO', 'Todos os requisitos atendidos', NOW()),
(2, 5, '33333333-3333-3333-3333-333333333333',
 'REJEITADO', 'Documentação incompleta', NOW())
ON CONFLICT (id) DO NOTHING;

SELECT setval('cidades_id_seq', COALESCE((SELECT MAX(id) FROM cidades), 0) + 1, false);
SELECT setval('bicos_id_seq', COALESCE((SELECT MAX(id) FROM bicos), 0) + 1, false);
SELECT setval('candidatura_id_seq', COALESCE((SELECT MAX(id) FROM candidatura), 0) + 1, false);
SELECT setval('hist_aprov_id_seq', COALESCE((SELECT MAX(id) FROM hist_aprov), 0) + 1, false);
