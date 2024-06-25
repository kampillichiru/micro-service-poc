name: Java CI with Ant

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout code
      uses: actions/checkout@v3

    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        distribution: 'temurin'
        java-version: '11'

    - name: Cache Ant dependencies
      uses: actions/cache@v3
      with:
        path: ~/.ivy2/cache
        key: ${{ runner.os }}-ant-${{ hashFiles('**/*.xml') }}
        restore-keys: |
          ${{ runner.os }}-ant-

    - name: Install Ant
      run: sudo apt-get install ant -y

    - name: Build with Ant
      run: ant -f build.xml
