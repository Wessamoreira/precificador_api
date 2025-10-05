CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE products (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(120) NOT NULL,
    sku VARCHAR(60) NOT NULL,
    default_purchase_cost NUMERIC(12,2) NOT NULL,
    default_packaging_cost NUMERIC(12,2) NOT NULL,
    default_other_variable_cost NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (owner_id, sku)
);

CREATE TABLE cost_items (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    description VARCHAR(100) NOT NULL,
    type VARCHAR(30) NOT NULL,
    amount_monthly NUMERIC(12,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE freight_batches (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    batch_size INT NOT NULL CHECK (batch_size > 0),
    freight_total NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE pricing_profiles (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    method VARCHAR(10) NOT NULL,
    markup NUMERIC(6,4),
    margin_on_price NUMERIC(6,4),
    machine_fee_pct NUMERIC(6,4) NOT NULL DEFAULT 0,
    marketplace_fee_pct NUMERIC(6,4) NOT NULL DEFAULT 0,
    other_fees_pct NUMERIC(6,4) NOT NULL DEFAULT 0,
    monthly_sales_target INT NOT NULL CHECK (monthly_sales_target > 0),
    rounding_rule VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Índices para otimizar consultas por usuário
CREATE INDEX idx_products_owner_id ON products(owner_id);
CREATE INDEX idx_cost_items_owner_id ON cost_items(owner_id);
CREATE INDEX idx_pricing_profiles_owner_id ON pricing_profiles(owner_id);