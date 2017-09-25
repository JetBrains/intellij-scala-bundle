#!/bin/bash
(
trap - SIGINT
exec /bin/bash bin/idea.sh "$@"
) &