#version
echo -n "-> Compute versionCode/buildNumber from versionName(1.0.0): "
read version
git rev-list --remotes=*origin/master --remotes=*origin/develop --remotes=*origin/release/* --remotes=*origin/hotfix/* --no-merges --count | xargs echo "-> VersionCode/BuildNumber is: "
