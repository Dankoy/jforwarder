#!/bin/bash

## VARS

EXTENSION="template"
TEMPLATE_DIR="project/deployments/templates"
DEPLOYMENT_DIR="project/deployments"
USER= #docker user 
REGISTRY_HOST="docker.io" # docker hub
TAG= #image registry

####### FUNCTIONS ##############

Help()
{
   # Display Help
   echo "Creates deployments from templates"
   echo
   echo "Syntax: scriptTemplate [-r|h|H|t|u]"
   echo "options:"
   echo "h     Print this help."
   echo "r     Choose repository."
   echo "H     Registry domain"
   echo "t     Tag to use."
   echo "u     User for repositories."
   echo
}

### SHELL

function apply_shell() {
   if [[ $1 == "zsh" ]]; then
        exec zsh
    elif [[ $1 == "bash" ]]; then
        exec bash
    else 
        printf "No valid shell\n"
        printf "Aporting\n"
        exit 1
    fi
} 

### REPLACE STUFF AND CREATE NEW FILE #####

function replace() {
    template=$1
    user=$2
    tag=$3

    if [ "$template" == "telegram-bot-deployment" ]; then
      image=$(echo "$template" | tr "-" "_")
      image=$(echo "${image%_*}" )
      image=$(echo "coub_forwarder_${image}")
    else 
      image=$(echo "$template" | tr "-" "_") # replace - with _
      image=$(echo "${image%_*}" ) # remove last word after _
    fi

    sed -e "s/__REGISTRY__/${REGISTRY_HOST}/g" -e "s/__USER__/${user}/g" -e "s/__IMAGE__/${image}/g" -e "s/__TAG__/${tag}/g" ${TEMPLATE_DIR}/"${template}".${EXTENSION} > ${DEPLOYMENT_DIR}/"${template}".yaml
}

### MAIN FUNC

function main() {

    printf "\nWorking with\n registry: %s\n user: %s\n tag: %s\n\n" "${REGISTRY_HOST}" "${USER}" "${TAG}"

    ## Make deployments from template files

    # find templates without extension and folder
    templates_without_extension=($(find ./"$TEMPLATE_DIR" -type f -name "*.template" -exec basename {} .template ';'))

    for template in "${templates_without_extension[@]}"; do
        printf "Processing %s\n" "$template"

        replace "$template" "$USER" "$TAG" "$REGISTRY_HOST"

        # do replace 

    done;
}



### CHECK ARGUMENTS

function check_args() {
    if [ -z "$TAG" ] || [ -z "$USER" ]; then
        echo 'Missing -t and -u' >&2
        exit 1
    fi
}

############## OPTIONS ###############
############## START MAIN ################

OPTSTRING=":t:H:u:h"

# Get the options
while getopts ${OPTSTRING} opt; do
  case ${opt} in
    h)
      Help
      exit 0
      ;;
    t)
      printf "Tag requested: %s \n" "${OPTARG}"
      TAG=${OPTARG}
      ;;
    H)
      printf "Registry host requested: %s \n" "${OPTARG}"
      REGISTRY_HOST=${OPTARG}
      ;;
    u)
      printf "User requested: %s \n" "${OPTARG}"
      USER=${OPTARG}
      ;;
    :)
      printf "Option -%s requires an argument. \n" "${OPTARG}"
      exit 1
      ;;
    ?)
      printf "Invalid option: -%s. \n" "${OPTARG}"
      exit 1
      ;;
  esac
done

check_args
main
