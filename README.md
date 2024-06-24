name: Release

on:
  push:
    tags:
      - 'v*'  # Triggers the workflow on version tags

jobs:
  release:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout code
      uses: actions/checkout@v3
      with:
        token: ${{ secrets.GITHUB_TOKEN }}

    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        distribution: 'temurin'
        java-version: '11'

    - name: Configure Git to use token
      run: |
        git config --global user.name 'github-actions[bot]'
        git config --global user.email 'github-actions[bot]@users.noreply.github.com'
        git remote set-url origin https://x-access-token:${{ secrets.GITHUB_TOKEN }}@github.com/${{ github.repository }}.git

    - name: Run Maven release
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
      run: mvn release:prepare release:perform
