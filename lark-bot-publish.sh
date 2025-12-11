#!/bin/sh

# -----------------------------
# 0. CHECK VARIABLES
# -----------------------------

# --- Check Allure Endpoint ---
if [ -z "$ALLURE_ENDPOINT" ]; then
  echo "Error: ALLURE_ENDPOINT is undefined. Skipping Lark notification."
  exit 0
fi

# --- Check Allure Token ---
if [ -z "$ALLURE_TOKEN" ]; then
  echo "Error: ALLURE_TOKEN is undefined. Skipping Lark notification."
  exit 0
fi

# --- Check Allure Launch ID ---
if [ -z "$ALLURE_LAUNCH_ID" ]; then
  echo "Error: ALLURE_LAUNCH_ID is undefined. Skipping Lark notification."
  exit 0
fi

# --- Check Lark Webhook URL ---
if [ -z "$LARK_BOT_WEBHOOK_URL" ]; then
  echo "Error: LARK_BOT_WEBHOOK_URL is undefined. Skipping Lark notification."
  exit 0
fi


# -----------------------------
# 1. GET TEST SUMMARY FROM ALLURE
# -----------------------------

# Normalize endpoint: remove ending slash if exists
NORMALIZED_ENDPOINT="${ALLURE_ENDPOINT%/}"

SUMMARY_JSON=$(wget -q \
  --header="Authorization: Api-Token $ALLURE_TOKEN" \
  -O - \
  "$NORMALIZED_ENDPOINT/api/launch/$ALLURE_LAUNCH_ID/statistic")

# Example:
# [{"status":"failed","count":49},{"status":"broken","count":98},{"status":"passed","count":141},{"status":"skipped","count":2}]

if [ -z "$SUMMARY_JSON" ]; then
  echo "Error: Allure returned empty response. Skipping."
  exit 0
fi

# -----------------------------
# 2. PARSE JSON
#    Extract counts for each status
# -----------------------------

extract_count() {
  STATUS="$1"

  COUNT=$(printf "%s" "$SUMMARY_JSON" \
    | grep -o "\"status\":\"$STATUS\"[^}]*" \
    | grep -o "\"count\"[[:space:]]*:[[:space:]]*[0-9]*" \
    | sed 's/.*://')

  echo "${COUNT:-0}"
}

FAILED=$(extract_count failed)
BROKEN=$(extract_count broken)
PASSED=$(extract_count passed)
SKIPPED=$(extract_count skipped)


# # -----------------------------
# # 3. PREPARE PAYLOAD FOR LARK (POST MESSAGE)
# # -----------------------------

TOTAL=$((FAILED + BROKEN + PASSED + SKIPPED))
NOT_PASSED=$((FAILED + BROKEN))


PAYLOAD="{
  \"msg_type\": \"post\",
  \"content\": {
    \"post\": {
      \"en_us\": {
        \"title\": \"📊 E2E Tests Summary (Launch $ALLURE_LAUNCH_ID)\",
        \"content\": [
          [{
            \"tag\": \"text\",
            \"text\": \"Total tests:  $TOTAL\n\n\"
          },
          {
            \"tag\": \"text\",
            \"text\": \"✔ Passed:  $PASSED\n\"
          },
          {
            \"tag\": \"text\",
            \"text\": \"❌ Failed:  $NOT_PASSED\n\"
          },
          {
            \"tag\": \"text\",
            \"text\": \"⏭ Skipped:  $SKIPPED\n\n\"
          },
          {
            \"tag\": \"a\",
            \"text\": \"More details\",
            \"href\": \"$NORMALIZED_ENDPOINT/launch/$ALLURE_LAUNCH_ID\"
          }]
        ]
      } 
    }
  }
}"

# -----------------------------
# 4. SEND MESSAGE TO LARK BOT
# -----------------------------

RESPONSE=$(wget -q \
  --header="Content-Type: application/json" \
  --post-data="$PAYLOAD" \
  -O - \
  "$LARK_BOT_WEBHOOK_URL")

if echo "$RESPONSE" | grep -q '"code":0'; then
  echo "Message sent to Lark."
else
  echo "Lark error: $RESPONSE"
fi