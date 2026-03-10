#!/bin/bash

# Configuration - Update these to match your environment
CLUSTER_NAME="orl-clust"       # Your Kafka cluster name
NAMESPACE="kafka-streams"       # The namespace where Kafka is running
TS_PASSWORD="123456"          # Password for your new truststore
TS_FILE="truststore.jks"        # Output filename

echo "--- Extracting Cluster CA Certificate ---"
# 1. Download the public CA certificate from the OpenShift Secret
oc get secret "${CLUSTER_NAME}-cluster-ca-cert" \
  -n "$NAMESPACE" \
  -o jsonpath='{.data.ca\.crt}' | base64 --decode > ca.crt

echo "--- Creating JKS Truststore ---"
# 2. Delete the old truststore if it exists
rm -f "$TS_FILE"

# 3. Import the certificate into a new JKS truststore
keytool -importcert \
  -alias strimzi-cluster-ca \
  -file ca.crt \
  -keystore "$TS_FILE" \
  -storepass "$TS_PASSWORD" \
  -noprompt

echo "--- Cleanup ---"
rm ca.crt

echo "Success! Your truststore is ready at: $(pwd)/$TS_FILE"