#!/bin/sh

# Configuration
TASKS_FOLDER=${TASKS_FOLDER:-'tasks'}
WORKFLOWS_FOLDER=${WORKFLOWS_FOLDER:-'workflows'}
CONDUCTOR_METADATA_URL=${CONDUCTOR_METADATA_URL:-'http://localhost:7080/api/metadata'}

register_task()
{
    # Try and register task
    _task_file=$1
    printf "\n-- Registering task       %s : " "$_task_file"
    _output=$(curl --silent --write-out 'HTTPSTATUS:%{http_code}' -X POST -H "Content-Type:application/json" "$CONDUCTOR_METADATA_URL/taskdefs" -d "@$_task_file")

    # Extract body and HTTP status
    _body=$(echo "$_output" | sed -e 's/HTTPSTATUS:.*//')
    _status=$(echo "$_output" | sed -n -e 's/.*HTTPSTATUS://p')

    # Check HTTP status code
    if [ "$_status" -eq 200 ] || [ "$_status" -eq 201 ]; then
        printf "OK\n"
    else
        printf "ERROR: %s \n" "$_body"
        return 1
    fi
}

register_workflow()
{
    # Try and register workflow
    _workflow_file=$1
    printf "\n-- Registering workflow   %s : " "$_workflow_file"
    _output=$(curl --silent --write-out 'HTTPSTATUS:%{http_code}' -X POST -H "Content-Type:application/json" "$CONDUCTOR_METADATA_URL/workflow" -d "@$_workflow_file")

    # Extract body and HTTP status
    _body=$(echo "$_output" | sed -e 's/HTTPSTATUS:.*//')
    _status=$(echo "$_output" | sed -n -e 's/.*HTTPSTATUS://p')

    # Check HTTP status code
    if [ "$_status" -eq 200 ] || [ "$_status" -eq 201 ]; then
        printf "OK\n"
    elif [ "$_status" -eq 409 ]; then
        printf "OK (already registered)\n"
    else
        printf "ERROR: %s\n" "$_body"
        return 1
    fi
}

main() 
{
    error_flag=0

    for file in "$TASKS_FOLDER"/*.json
    do
        if [ -e "$file" ]; then
            register_task "$file" || error_flag=1
        else
            break
        fi
    done

    for file in "$WORKFLOWS_FOLDER"/*.json
    do
        if [ -e "$file" ]; then
            register_workflow "$file" || error_flag=1
        else
            break
        fi
    done

    exit $error_flag
}

main "$@"