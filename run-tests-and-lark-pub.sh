#!/bin/sh

# -----------------------------
# 0. CHECK PARAMETER
# -----------------------------

TEST_TASK="$1"

if [ -z "$TEST_TASK" ]; then
  echo "Error: No test task specified."
  echo "Usage: $0 <gradle-test-task>"
  exit 1
fi


# -----------------------------
# 1. RUN TESTS
# -----------------------------

set +e  # allow command failure

./allurectl watch -- gradle clean "$TEST_TASK"
WATCH_EXIT_CODE=$?

set -e  # restore strict mode

echo "Tests exit code: $WATCH_EXIT_CODE"

export $(./allurectl job-run env)

chmod +x ./lark-bot-publish.sh
./lark-bot-publish.sh

if [ "$WATCH_EXIT_CODE" -ne 0 ]; then
  echo "❌ TESTS FAILED"
else
  echo "✅ TESTS PASSED"
fi
