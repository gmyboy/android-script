#update all submodule
git submodule foreach git remote update origin -p
#update main repo
git remote update origin -p