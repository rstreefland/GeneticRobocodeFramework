rm -rf ./workers/

mkdir ./workers
mkdir ./workers/worker1
mkdir ./workers/worker2
mkdir ./workers/worker3
mkdir ./workers/worker4

cp -rf ./lib/ ./workers/worker1/
cp -f battleRunner.jar workers/worker1

cp -rf ./lib/ ./workers/worker2/
cp -f battleRunner.jar workers/worker2

cp -rf ./lib/ ./workers/worker3/
cp -f battleRunner.jar workers/worker3

cp -rf ./lib/ ./workers/worker4/
cp -f battleRunner.jar workers/worker4

cd /Users/Rhys/Development/IdeaProjects/GARobocode/workers/worker1/
java -jar battleRunner.jar 15001 &
cd /Users/Rhys/Development/IdeaProjects/GARobocode/workers/worker2/
java -jar battleRunner.jar 15002 &
cd /Users/Rhys/Development/IdeaProjects/GARobocode/workers/worker3/
java -jar battleRunner.jar 15003 &
cd /Users/Rhys/Development/IdeaProjects/GARobocode/workers/worker4/
java -jar battleRunner.jar 15004 &

trap 'kill $(jobs -p)' EXIT
wait
