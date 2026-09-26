#!/usr/bin/env python3
import sys
import xml.etree.ElementTree as ET
from pathlib import Path


def read_keys(strings_file):
    return {node.get("name") for node in ET.parse(strings_file).getroot()}


def find_default_files(root):
    return [
        path
        for path in root.rglob("composeResources/values/strings.xml")
        if "build" not in path.parts
    ]


def compare(default_file, localized_file):
    expected = read_keys(default_file)
    actual = read_keys(localized_file)
    problems = [f"{localized_file}: missing '{key}'" for key in sorted(expected - actual)]
    problems += [f"{localized_file}: extra '{key}'" for key in sorted(actual - expected)]
    return problems


def main():
    default_files = find_default_files(Path("."))
    if not default_files:
        print("No composeResources/values/strings.xml found")
        return 1

    problems = []
    for default_file in default_files:
        for localized_file in default_file.parent.parent.glob("values-*/strings.xml"):
            problems += compare(default_file, localized_file)

    for problem in problems:
        print(problem)
    if problems:
        return 1

    print("OK: all locales have the same keys")
    return 0


if __name__ == "__main__":
    sys.exit(main())
