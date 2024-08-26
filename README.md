# etl

This project contains functions for data engineering, implemented in the Clojure programming language. 

## Usage

##### run the app
```bash
lein run
```

##### test the app
```bash
lein test
```

##### build/package the app, then run 
```bash
lein uberjar
...
java -jar /target/uberjar/etl-0.1.0-SNAPSHOT-standalone.jar [args]
```

### TODOs
0. Data Ingestion/Extraction (E in ETL) Functions
1. Data Validation and Quality (* in ETL) Functions
2. Data Transformation (T in ETL) Functions
    parsing, cleaning, filter, aggregation, joining, enrichment, etc
3. Data Loading (L in ETL) Functions
4. Data Monitoring and Logging (* in ETL) Functions
5. Data Pipeline Orchestration (* in ETL) Functions
