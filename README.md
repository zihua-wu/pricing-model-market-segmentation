# Pricing Model: Market Segmentation

Java model for customer market segmentation and sales-channel assignment, with mock business data generation.

Market Segmentation Logic:

Customers are classified into three markets using meaningful business attributes:

Young Budget Shoppers: age < 30 and income = LOW

Rising Professionals: age 30–39 and income = MEDIUM

Premium Loyal Customers: age ≥ 40 or income = HIGH

Each customer belongs to exactly one market.

Channel Assignment:

Each market has exactly two channels.
One channel (Mobile App) is intentionally shared between two markets.

Young Budget Shoppers → Social Media Ads, Mobile App

Rising Professionals → Email Campaign, Mobile App

Premium Loyal Customers → Account Manager, Corporate Website

Customers are assigned to one of the channels within their market using a clear, consistent rule (random selection between the two valid channels).

System Architecture:

SupplierDirectory —— contains multiple Supplier

Supplier —— has ProductCatalog

ProductCatalog —— has multiple Product

Product —— has floor/target/ceiling price

CustomerDirectory —— has multiple Customer

Customer —— has Orders + segmentation attributes + Market + Channel

Order —— has multiple OrderItem

OrderItem —— links Product + quantity + paidPricePerItem


MasterOrderList —— has all Orders


MarketDirectory —— has multiple Market (≥3)

Market —— has exactly 2 Channels

Channel —— may be shared between Markets


Business —— data generator + assignment of Market & Channel


ReportGenerator —— create:

    Report 1: revenue by Market (inner sorted by Channel)

    Report 2: revenue by Channel (across all Markets)
