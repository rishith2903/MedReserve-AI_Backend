#!/bin/bash

# MedReserve Backend Deployment Test Script
# Tests all critical endpoints after deployment

echo "🏥 MedReserve Backend Deployment Test"
echo "====================================="

# Configuration
BASE_URL="${1:-http://localhost:8080/api}"
echo "Testing API at: $BASE_URL"
echo ""

# Test 1: Health Check
echo "1️⃣  Testing Health Check..."
HEALTH_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/actuator/health")
if [ "$HEALTH_RESPONSE" = "200" ]; then
    echo "✅ Health check passed (HTTP $HEALTH_RESPONSE)"
    curl -s "$BASE_URL/actuator/health" | jq '.' 2>/dev/null || echo "Response received"
else
    echo "❌ Health check failed (HTTP $HEALTH_RESPONSE)"
fi
echo ""

# Test 2: Test Endpoint
echo "2️⃣  Testing API Test Endpoint..."
TEST_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/test")
if [ "$TEST_RESPONSE" = "200" ]; then
    echo "✅ Test endpoint passed (HTTP $TEST_RESPONSE)"
    curl -s "$BASE_URL/test" | jq '.' 2>/dev/null || echo "Response received"
else
    echo "❌ Test endpoint failed (HTTP $TEST_RESPONSE)"
fi
echo ""

# Test 3: Ping Endpoint
echo "3️⃣  Testing Ping Endpoint..."
PING_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/test/ping")
if [ "$PING_RESPONSE" = "200" ]; then
    echo "✅ Ping endpoint passed (HTTP $PING_RESPONSE)"
    curl -s "$BASE_URL/test/ping" | jq '.' 2>/dev/null || echo "Response received"
else
    echo "❌ Ping endpoint failed (HTTP $PING_RESPONSE)"
fi
echo ""

# Test 4: Swagger UI
echo "4️⃣  Testing Swagger UI..."
SWAGGER_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/swagger-ui.html")
if [ "$SWAGGER_RESPONSE" = "200" ]; then
    echo "✅ Swagger UI accessible (HTTP $SWAGGER_RESPONSE)"
else
    echo "❌ Swagger UI failed (HTTP $SWAGGER_RESPONSE)"
fi
echo ""

# Test 5: API Docs
echo "5️⃣  Testing API Documentation..."
DOCS_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api-docs")
if [ "$DOCS_RESPONSE" = "200" ]; then
    echo "✅ API docs accessible (HTTP $DOCS_RESPONSE)"
else
    echo "❌ API docs failed (HTTP $DOCS_RESPONSE)"
fi
echo ""

# Summary
echo "📊 Test Summary"
echo "==============="
TOTAL_TESTS=5
PASSED_TESTS=0

[ "$HEALTH_RESPONSE" = "200" ] && ((PASSED_TESTS++))
[ "$TEST_RESPONSE" = "200" ] && ((PASSED_TESTS++))
[ "$PING_RESPONSE" = "200" ] && ((PASSED_TESTS++))
[ "$SWAGGER_RESPONSE" = "200" ] && ((PASSED_TESTS++))
[ "$DOCS_RESPONSE" = "200" ] && ((PASSED_TESTS++))

echo "Passed: $PASSED_TESTS/$TOTAL_TESTS tests"

if [ "$PASSED_TESTS" = "$TOTAL_TESTS" ]; then
    echo "🎉 All tests passed! Backend is ready for production."
    exit 0
else
    echo "⚠️  Some tests failed. Check the logs above."
    exit 1
fi
