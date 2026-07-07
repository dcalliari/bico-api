ALTER TABLE candidatura
DROP CONSTRAINT IF EXISTS candidatura_status_check;

ALTER TABLE candidatura 
ADD CONSTRAINT candidatura_status_check 
CHECK (status IN ('PENDENTE', 'AGUARDANDO_N2', 'AGUARDANDO_N3', 'APROVADO', 'REJEITADO', 'DEVOLVIDO'));