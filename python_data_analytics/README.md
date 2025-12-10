# Python Data Analytics: Customer Behaviour and Sales Insights

## Introduction
London Gift Shop (LGS) is a UK-based online retailer that sells giftware to both individual customers and wholesalers. Despite operating online for over ten years, the company has seen little revenue growth recently. To improve customer engagement and boost sales, the LGS marketing team wants to use data analytics to better understand how customers shop.

LGS partnered with Jarvis Consulting Group to develop a PoC that explores how data analytics can support their goals. My role was to analyze two years of historical transaction data and extract insights on customer activity, purchasing patterns, seasonality, and customer value. These insights will help the marketing team design targeted campaigns, personalized promotions, and strategies to retain existing customers while attracting new ones.

This PoC demonstrates how data analytics can transform raw transactional data into meaningful business intelligence. The final deliverables include RFM metrics, monthly sales trends, active user behaviour, and insights on new vs. returning customers.

**Technologies and Work Completed**
- Used Jupyter Notebook as the main environment for exploration, analysis, and visualization.
- Processed and analyzed data using Python with libraries such as Pandas, NumPy, and Matplotlib.
- Loaded and managed transactional data stored in PostgreSQL, running inside Docker containers to simulate a lightweight data warehouse environment.
- Performed data-wrangling tasks including type casting, handling missing values, feature creation, and deriving monthly/behavioral metrics.
- Applied data-warehouse style thinking by organizing datasets into structured analytical outputs.
- Computed key customer insights, including recency, frequency, monetary value, monthly sales trends, active users, and new vs. returning customers.
- Built visualizations to help the LGS marketing team understand customer behaviour and support data-driven campaign planning.


## Implementation
### Project Architecture

The LGS e-commerce platform is deployed on Azure, and the Jarvis Data Analytics PoC operates as a separate analytical environment.

- **Front-end (Web App):** Customers access the store through a browser. Static assets such as HTML, CSS, JavaScript, and product images are delivered through a CDN and stored in Azure Blob Storage.

- **Backend (API Stack):** API requests are routed through Azure API Management and handled by microservices running on Azure Kubernetes Service (AKS). All transactional activity is recorded in an Azure SQL Server (OLTP) database.

- **Data Extraction for Analytics:** Because the PoC cannot interact with the live Azure systems, the LGS IT team exported two years of anonymized transactional data into a SQL dump (`retail.sql`), serving as a simplified data-warehouse extract.

- **Jarvis Analytics Environment:** The file is loaded into a PostgreSQL database running in Docker. A Jupyter Notebook connects to this warehouse to perform data wrangling, visualization, and analytics such as sales trends, customer activity, and RFM metrics.


### Data Analytics and Wrangling
See the full Jupyter Notebook: [Retail Data Analytics & Wrangling Notebook](./retail_data_analytics_wrangling.ipynb)

RFM analysis highlights three key customer groups that LGS can target to increase revenue:

#####  1. High-Value Customers

They purchase often and spend the most.
- Champions: very recent buyers (~7 days), very frequent, highest spend
- Loyal Customers: recent buyers (~66 days), high frequency

**Strategy:**
Reward them with VIP offers, personalized recommendations, and referral incentives.

##### 2. Growing Customers

They show good recent activity and moderate spending.
- Potential Loyalists: recent purchases (~24 days), increasing frequency

**Strategy:**
Encourage them toward loyalty with targeted promotions and product suggestions.

##### 3. At-Risk Customers
Customers who used to be valuable but haven’t purchased recently.

- At Risk: very long inactivity (~375 days), strong past spending

- Can’t Lose: long inactivity (~319 days), very high historical frequency and spending

**Strategy:**
Use targeted win-back campaigns that include personalized product recommendations based on past purchases or viewed items. Highlight new or popular products similar to what they previously showed interest in. For high-value customers like Can’t Lose, reach out directly to understand why they became inactive and offer a small incentive or gift for providing feedback.

#### Business Impact
Focusing on these three groups helps LGS:
- Protect its highest revenue contributors
- Convert mid-tier customers into loyal buyers
- Recover revenue from customers at risk of churn

## Improvements
##### 1. Build an automated ETL pipeline
Develop a fully automated pipeline to ingest new transactional data from source systems, clean and validate records, and load them into a structured data warehouse on a recurring schedule. This would eliminate manual processing, ensure data freshness, and allow LGS to continuously track customer behaviour and sales performance.


##### 2. Deploy dashboard
Create dynamic dashboards using tools like Tableau or Power BI to give the LGS marketing team real-time access to sales trends, customer insights, RFM segments, and user activity metrics. This would allow non-technical users to explore the data visually, filter by customer groups or time periods, and make faster, data-driven decisions.

##### 3. Implement advanced customer analytics:
Expand the analysis beyond basic RFM metrics by applying machine learning techniques such as clustering for customer segmentation, forecasting models for sales prediction, or churn-prediction models to identify at-risk customers. These insights would allow LGS to design more precise and proactive marketing strategies.


