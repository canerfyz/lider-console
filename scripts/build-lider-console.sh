#!/bin/bash

###
# This script exports the Lider Console product for Linux (x32, x64), Windows (x32, x64) and MacOSX.
#
# Exported products can be found under /tmp/lider-console
###

PRJ_ROOT_PATH=$(readlink -f ..)
echo "Project path: $PRJ_ROOT_PATH"

# Generate third-party dependencies
echo "Generating third-party dependencies..."
cd "$PRJ_ROOT_PATH"/lider-console-dependencies
mvn clean p2:site
echo "Generated third-party dependencies."

# Start jetty server for Tycho to use generated dependencies
echo "Starting server for Tycho..."
mvn jetty:run &
J_PID=$!
echo "Started server."

# While server is running, start Tycho
echo "Building lider-console project..."
cd "$PRJ_ROOT_PATH"
mvn clean install -DskipTests
echo "lider-console project built successfully."

# After exporting products, kill jetty server process
echo "Shutting down server..."
kill $J_PID
echo "Server shut down."

EXPORT_PATH=/tmp/lider-console
echo "Export path: $EXPORT_PATH"

echo "Copying exported lider-console products to $EXPORT_PATH..."
mkdir -p "$EXPORT_PATH"
cp -rf "$PRJ_ROOT_PATH"/lider-console-products/target/products/. "$EXPORT_PATH"
echo "Copied exported lider-console products."

echo "Built finished successfully!"
echo "Products can be found under: $EXPORT_PATH"
