# this requires special cli
url=https://docs.confluent.io/current/cli/command-reference/confluent-local/confluent_local_produce.html
if [[ "$(which confluent)" == "" ]]; then
  echo "you need to install confluent cli for this to work: ${url}"
  else
  confluent local produce inputTopic -- \
    --value-format avro \
    --property value.schema="$(cat src/main/avro/evolution-value.avsc)"
fi
