# 1. Your Secret (must match the 'token' in your quarkus-hello-github-webhook-secret)
SECRET="X-x-X-x-X"

# 2. The JSON payload (must match the params your TriggerBinding expects)
DATA='{
  "repository": {
    "clone_url": "https://github.com/your-user/quarkus-hello.git"
  },
  "head_commit": {
    "id": "main"
  }
}'

# 3. Generate the HMAC SHA1 signature that GitHub usually sends
# This creates a string like: sha1=7d86...
SIG="sha1=$(echo -n "$DATA" | openssl dgst -sha1 -hmac "$SECRET" | cut -d' ' -f2)"

# 4. Fire the request
curl -v -k -X POST \
  -H "X-GitHub-Event: push" \
  -H "X-Hub-Signature: $SIG" \
  -H "Content-Type: application/json" \
  -d "$DATA" \
  https://quarkus-hello-build.apps.cluster-492gk.492gk.sandbox2076.opentlc.com