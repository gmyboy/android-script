#version
echo -n "请输入版本号: "
read version

git rev-list --remotes=*origin/master --remotes=*origin/develop --remotes=*origin/release/* --remotes=*origin/hotfix/* --no-merges --count | xargs echo "Commit count from remote : "
