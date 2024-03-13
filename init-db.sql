-- init-db.sql
USE bankdesafio;

CREATE TABLE IF NOT EXISTS tbl_conta_corrente (
  id CHAR(36) NOT NULL,
  saldo DECIMAL(38,2) NOT NULL,
  ativa BOOLEAN NOT NULL,
  limite_diario DECIMAL(38,2) NOT NULL DEFAULT '1000.00',
  total_transferido_hoje DECIMAL(38,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (id)
);

INSERT INTO tbl_conta_corrente (id, saldo, ativa, limite_diario, total_transferido_hoje) VALUES
('7859ee7a-edb5-416b-80c9-f6840007ddce', 1000.00, TRUE, 1000.00, 0.00),
('2fd0f2f6-58f3-4c8b-b510-ec20f6447b44', 2000.00, TRUE, 1000.00, 0.00),
('533caec7-e28b-46be-a796-34fff27c3b63', 3000.00, FALSE, 1000.00, 0.00);

CREATE TABLE IF NOT EXISTS tbl_transacao (
  id CHAR(36) NOT NULL,
  valor DECIMAL(38,2) NOT NULL,
  data_transacao DATETIME NOT NULL,
  nome_cliente_origem VARCHAR(255) NOT NULL,
  nome_cliente_destino VARCHAR(255) NOT NULL,
  conta_origem_id CHAR(36) NOT NULL,
  conta_destino_id CHAR(36) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_transacao_conta_origem FOREIGN KEY (conta_origem_id) REFERENCES tbl_conta_corrente(id),
  CONSTRAINT fk_transacao_conta_destino FOREIGN KEY (conta_destino_id) REFERENCES tbl_conta_corrente(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
