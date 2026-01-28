## Introduction
In a previous project, I built a data analytics solution for the **London Gift Shop (LGS)** marketing team using **Jupyter Notebook and Python**. That solution provided valuable customer insights (such as RFM segmentation) that helped the marketing team design targeted campaigns and improve customer retention.

As LGS scaled its operations, the existing single-machine notebook solution became a limitation when working with larger datasets. To support company-wide analytics and higher data volumes, the solution was re-architected using **Apache Spark**, a distributed data processing framework.

The goal of this project is to migrate the existing analytics logic to **Databricks on Azure**, leveraging its managed Spark environment to enable scalable data processing, efficient transformations, and production-ready analytics. The project focuses on designing a cloud-based Spark architecture, implementing customer analytics with PySpark, and generating actionable insights for the marketing t


## Databricks Implementation

### Dataset

The dataset used in this implementation is the **London Gift Shop transactional dataset**, containing historical sales records with the following attributes:

- Invoice number  
- Customer ID  
- Transaction timestamp
- Quantity purchased  
- Unit price
- Product description  

The dataset represents real-world retail transactions and is well suited for customer behavior and marketing analytics.

### Analytics Work

All analytics were implemented using **PySpark on Databricks**. The main steps include:

- Loading raw transactional data from cloud storage  
- Data cleaning and preprocessing (null handling, filtering invalid records)  
- Customer-level aggregations based on purchase behavior  
- Computation of **RFM metrics** (Recency, Frequency, Monetary)  
- Customer segmentation using regex rules derived from RFM scores  
- Identification of high-value and at-risk customer segments such as *Champions*, *Can’t Lose*, and *Hibernating*  

### Architecture and Data Flow

The solution is built on a **cloud-native Databricks architecture**:

- **Azure Cloud Storage** stores the raw transactional data  
- **Databricks Workspace** provides a managed Apache Spark environment  
- **DBFS (Databricks File System)** abstracts access to cloud storage  
- **PySpark** is used for distributed data processing and analytics  
- **Hive Metastore** manages table schemas and metadata  

**Data Flow Overview**:
1. Raw CSV data is stored in Azure cloud storage  
2. Databricks accesses the data through DBFS  
3. PySpark performs distributed cleaning and transformations  
4. Aggregated and segmented datasets are registered in the Hive Metastore  
5. Results are used for analysis and business insight generation  


## Future Improvements

- Automate ingestion and transformations using orchestration tools (Airflow or Databricks Workflows)  
- Add data quality checks and validation rules  
- Implement incremental data processing instead of full reloads  
- Integrate BI tools for business-facing dashboards  
