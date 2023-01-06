#update all submodule
echo "-> Sync branches focely to origin"
git submodule foreach git remote update origin -p
#update main repo
git remote update origin -p