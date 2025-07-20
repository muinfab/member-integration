#!/bin/bash

# PlantUML 다이어그램을 이미지로 생성하는 스크립트

echo "🚀 PlantUML 다이어그램 이미지 생성 시작..."

# PlantUML JAR 파일 경로
PLANTUML_JAR="$HOME/.plantuml/plantuml.jar"

# 다이어그램 소스 폴더
SOURCE_DIR="docs/sequence-diagrams"

# 이미지 출력 폴더
OUTPUT_DIR="docs/images"

# 출력 폴더 생성
mkdir -p "$OUTPUT_DIR"

# 모든 .puml 파일을 PNG로 변환
for file in "$SOURCE_DIR"/*.puml; do
    if [ -f "$file" ]; then
        filename=$(basename "$file" .puml)
        echo "📊 변환 중: $filename.puml → $filename.png"
        
        java -jar "$PLANTUML_JAR" -tpng "$file" -o "../../$OUTPUT_DIR"
        
        if [ $? -eq 0 ]; then
            echo "✅ 성공: $filename.png 생성 완료"
        else
            echo "❌ 실패: $filename.png 생성 실패"
        fi
    fi
done

echo "🎉 모든 다이어그램 변환 완료!"
echo "📁 생성된 이미지 위치: $OUTPUT_DIR/" 