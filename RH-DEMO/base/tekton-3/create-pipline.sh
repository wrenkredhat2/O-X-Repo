recreate() {
  ls $1 1>/dev/null  
  set +e
  oc delete --wait=true -f $1
  set -e

  oc create  -f $1 
}

set -e

recreate maven-build.yaml 
recreate maven-settings-cm.yaml 
recreate maven-pom-version-task.yaml
recreate git-clone-debug-task.yaml 
recreate tekton-buildah-task.yaml
recreate quarkus-pipeline.yaml

oc create -f pipeline-run.yaml