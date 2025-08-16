ALTER TABLE topico ADD CONSTRAINT uq_topico_titulo_mensaje UNIQUE (titulo(100), mensaje(100));
