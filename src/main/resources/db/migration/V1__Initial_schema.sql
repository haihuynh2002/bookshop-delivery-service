-- Create delivery table
CREATE TABLE delivery (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(20),

    exchange BOOLEAN DEFAULT FALSE,
    amount DOUBLE PRECISION NOT NULL,

    courier_id VARCHAR(50),
    courier_first_name VARCHAR(255),
    courier_last_name VARCHAR(255),
    courier_email VARCHAR(255),
    courier_phone VARCHAR(20),

    status VARCHAR(255) NOT NULL,

    shipping_street TEXT NOT NULL,
    shipping_city TEXT NOT NULL,
    shipping_state TEXT NOT NULL,
    shipping_postal_code TEXT NOT NULL,
    shipping_country TEXT NOT NULL,

    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    location VARCHAR(255),

    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

-- Create indexes for better performance
CREATE INDEX idx_delivery_order_id ON delivery(order_id);
CREATE INDEX idx_delivery_id ON delivery(id);
CREATE INDEX idx_delivery_courier_id ON delivery(courier_id);
CREATE INDEX idx_delivery_status ON delivery(status);
CREATE INDEX idx_delivery_created_date ON delivery(created_date);