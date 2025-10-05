-- V2__create_sales_and_customers_tables.sql

-- Tabela para armazenar informações dos clientes
CREATE TABLE customers (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (owner_id, phone_number) -- Telefone deve ser único por usuário
);

-- Tabela para registrar cada venda (transação)
CREATE TABLE sales (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    customer_id UUID NOT NULL REFERENCES customers(id),
    sale_date TIMESTAMP WITH TIME ZONE NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    total_net_profit NUMERIC(12, 2) NOT NULL
);

-- Tabela para os itens dentro de cada venda
CREATE TABLE sale_items (
    id UUID PRIMARY KEY,
    sale_id UUID NOT NULL REFERENCES sales(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id),
    quantity INT NOT NULL,
    -- "Snapshot" do preço e custo no momento da venda para histórico preciso
    unit_price NUMERIC(12, 2) NOT NULL,
    unit_cost_at_sale NUMERIC(12, 2) NOT NULL,
    net_profit NUMERIC(12, 2) NOT NULL
);

-- Índices para performance
CREATE INDEX idx_customers_owner_id ON customers(owner_id);
CREATE INDEX idx_sales_owner_id ON sales(owner_id);
CREATE INDEX idx_sales_customer_id ON sales(customer_id);