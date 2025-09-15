#!/bin/bash

# Test API Script untuk Job Type Service
# Pastikan aplikasi sudah running di port 8081

BASE_URL="http://localhost:8081/api/job-types"

echo "=== Testing Job Type Service API ==="
echo "Base URL: $BASE_URL"
echo ""

# Test 1: Create Job Type
echo "1. Testing CREATE Job Type..."
echo "POST $BASE_URL"
curl -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "code": "TEST_JOB",
    "description": "Test Job Type for API Testing",
    "updateBy": "test_user"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

echo ""
echo "---"

# Test 2: Get Job Type by ID (assuming ID 1 exists)
echo "2. Testing GET Job Type by ID..."
echo "GET $BASE_URL/1"
curl -X GET $BASE_URL/1 \
  -H "Accept: application/json" \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

echo ""
echo "---"

# Test 3: Update Job Type
echo "3. Testing UPDATE Job Type..."
echo "PUT $BASE_URL/1"
curl -X PUT $BASE_URL/1 \
  -H "Content-Type: application/json" \
  -d '{
    "code": "UPDATED_JOB",
    "description": "Updated Job Type Description",
    "updateBy": "test_user"
  }' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

echo ""
echo "---"

# Test 4: Get All Job Types
echo "4. Testing GET All Job Types..."
echo "GET $BASE_URL"
curl -X GET $BASE_URL \
  -H "Accept: application/json" \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

echo ""
echo "---"

# Test 4.5: Get Job Type by Code
echo "4.5. Testing GET Job Type by Code..."
echo "GET $BASE_URL/code/PART_TIME"
curl -X GET $BASE_URL/code/PART_TIME \
  -H "Accept: application/json" \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

echo ""
echo "---"

# Test 5: Delete Job Type (optional - be careful with this)
echo "5. Testing DELETE Job Type..."
echo "DELETE $BASE_URL/1"
echo "WARNING: This will delete the job type with ID 1"
read -p "Do you want to proceed with delete? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    curl -X DELETE $BASE_URL/1 \
      -w "\nHTTP Status: %{http_code}\n" \
      -s
else
    echo "Delete test skipped."
fi

echo ""
echo "=== API Testing Complete ==="
