#get commit count of remote branch 
git rev-list --remotes=*origin/master --remotes=*origin/develop --remotes=*origin/release/* --remotes=*origin/hotfix/* --no-merges --count | xargs echo "Commit count from remote : "
