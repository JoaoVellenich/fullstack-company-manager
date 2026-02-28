#!/bin/sh
cat <<EOF > /usr/share/nginx/html/browser/runtime-env.js
window.__env = {
  apiUrl: "${API_URL}"
};
EOF
exec nginx -g 'daemon off;'
