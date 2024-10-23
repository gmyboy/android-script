#!/bin/bash

# Echo
echo
echo
echo "   ╭──────────────────────────────────────────────────────────────────╮"
echo "   │                                                                  │"
echo "   │          Clean the Fotric maven cache of gradle in Android.      │"
echo "   │                             by GMY.                              │"
echo "   │                                                                  │"
echo "   ╰──────────────────────────────────────────────────────────────────╯"
echo
echo

# Check the directory of gradle maven cache
path="$GRADLE_USER_HOME"
GRADLE_MAVEN_CACHE_DIR="${path//\\/\/}/caches/modules-2/files-2.1"
echo "Gradle cache dir: ${GRADLE_MAVEN_CACHE_DIR}"

DELETE_PATTERNS=("cn.fotric.*" "com.fotric.*" "com.whale.*" "com.irtek.*")

# Check if the target directory exists
if [ -d "$GRADLE_MAVEN_CACHE_DIR" ]; then
    # Iterate through each delete pattern
    for pattern in "${DELETE_PATTERNS[@]}"; do
        # Use find command to find directories matching the pattern and delete them
        find "$GRADLE_MAVEN_CACHE_DIR" -type d -name "$pattern" -exec rm -rf {} +
        echo "Deleted: $GRADLE_MAVEN_CACHE_DIR/$pattern"
    done
    echo "Done"
else
    echo "Target directory does not exist"
fi