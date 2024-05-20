package org.example.attributeconverter18052024.feature.file;

import io.opencensus.resource.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.example.attributeconverter18052024.domain.UserDetail;
import org.example.attributeconverter18052024.feature.file.dto.FileResponse;
import org.example.attributeconverter18052024.feature.userdetail.UserDetailRepository;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${file.storage-dir}")
    private String fileStorageDir;

    private final UserDetailRepository userDetailRepository;


    private static final Set<String> SUPPORTED_FILE_TYPES = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private String generateUrl(HttpServletRequest request, String filename, String prefix) {
        return String.format("%s://%s:%d/%s/%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                prefix,
                filename);
    }

    private String readPdfContent(InputStream inputStream) {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            log.error("Error occurred while reading PDF content", e);
            throw new RuntimeException("Error occurred while reading PDF content", e);
        }
    }

    private String readDocxContent(InputStream inputStream) {
        try (XWPFDocument docx = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(docx)) {
            return extractor.getText();
        } catch (IOException e) {
            log.error("Error occurred while reading DOCX content", e);
            throw new RuntimeException("Error occurred while reading DOCX content", e);
        }
    }

    private String uploadFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (!SUPPORTED_FILE_TYPES.contains(contentType)) {
            log.warn("Unsupported file type: {}", contentType);
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, contentType + " not allowed!!");
        }

        try {
            Path fileStoragePath = Path.of(fileStorageDir);
            if (!Files.exists(fileStoragePath)) {
                Files.createDirectories(fileStoragePath);
            }
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String fileName = UUID.randomUUID() + fileExtension;


            Files.copy(file.getInputStream(), fileStoragePath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            log.info("File uploaded successfully: {}", fileName);
            return fileName;
        } catch (IOException ex) {
            log.error("Failed to store file", ex);
            throw new RuntimeException("Failed to store file", ex);
        }
    }

    @Override
    public List<String> getAllFileNames() {
        try {
            return Files.list(Path.of(fileStorageDir))
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error occurred while getting all file names", e);
            throw new RuntimeException("Error occurred while getting all file names", e);
        }
    }

    @Override
    public ResponseEntity<Resource> serveFile(String filename, HttpServletRequest request) {
        return null;
    }

    @Override
    public FileResponse uploadSingleFile(MultipartFile file, HttpServletRequest request) throws IOException {
        String filename = uploadFile(file);
        String fullImageUrl = generateUrl(request, filename, "images");
        String content = null;

        // Read content based on file type
        switch (Objects.requireNonNull(file.getContentType())) {
            case "application/pdf":
                content = readPdfContent(file.getInputStream());
                break;
            case "application/msword":
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                content = readDocxContent(file.getInputStream());
                break;
        }

        if (content == null) {
            content = "";
        }

        // Define keywords dynamically based on the file type or other criteria
        List<String> keywords = Arrays.asList("First Name", "Last Name", "Email", "Telephone", "Nationality", "Marital Status", "Height", "Health Status", "Place of Birth", "Languages", "Education", "Educational Qualifications", "Experience", "Skills", "Skills & Abilities", "Projects", "References", "Interests", "Achievements", "Date of Birth", "Religion", "Sex", "Gender");

        Map<String, String> resumeSections = extractInformation(content, keywords);

        resumeSections.forEach((key, value) -> log.info("{}: {}", key, value));

        UserDetail userDetail = new UserDetail();

        userDetail.setFirstName(resumeSections.get("First Name"));
        userDetail.setLastName(resumeSections.get("Last Name"));
        userDetail.setEmail(resumeSections.get("Email"));
        userDetail.setGender(resumeSections.get("Gender"));
        userDetail.setTelephone(resumeSections.get("Telephone"));

        userDetailRepository.save(userDetail);


        return new FileResponse(
                generateUrl(request, filename, "api/v1/files/download"),
                file.getContentType(),
                (float) file.getSize() / 1024, // in KB
                filename,
                fullImageUrl,
                content
        );
    }

    public static Map<String, String> extractInformation(String content, List<String> keywords) {
        Map<String, String> resumeSections = new LinkedHashMap<>();

        // Sort keywords by length in descending order to match longer phrases first
        keywords.sort((a, b) -> b.length() - a.length());
        String patternString = String.join("|", keywords.stream().map(Pattern::quote).collect(Collectors.toList()));

        Pattern pattern = Pattern.compile("(?i)\\b(" + patternString + ")\\b");
        Matcher matcher = pattern.matcher(content);


        String lastKeyword = null;
        int lastMatchEnd = 0;
        while (matcher.find()) {
            if (lastKeyword != null) {
                String sectionContent = content.substring(lastMatchEnd, matcher.start()).trim();
                resumeSections.put(lastKeyword, cleanContent(sectionContent));
            }
            lastKeyword = matcher.group(1);
            lastMatchEnd = matcher.end();
        }

        if (lastKeyword != null && lastMatchEnd < content.length()) {
            String lastSectionContent = content.substring(lastMatchEnd).trim();
            resumeSections.put(lastKeyword, cleanContent(lastSectionContent));
        }

        // Additional extraction for specific sections
        extractAdditionalInformation(content, resumeSections, "Email", "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
        extractAdditionalInformation(content, resumeSections, "Telephone", "\\+?[0-9. ()-]{7,}");
        extractAdditionalInformation(content, resumeSections, "First Name", "(?i)\\b(?:first\\s+name|given\\s+name):?\\s*(\\b[a-zA-Z]+\\b)");
        extractAdditionalInformation(content, resumeSections, "Last Name", "(?i)\\b(?:last\\s+name|surname):?\\s*(\\b[a-zA-Z]+\\b)");
        extractLanguagesInformation(content, resumeSections, "Languages");
        extractSkillsInformation(content, resumeSections, "Skills");
        extractAdditionalInformation(content, resumeSections, "Date of Birth", "(?i)\\b(?:date\\s+of\\s+birth|dob):?\\s*([0-9]{2}/[0-9]{2}/[0-9]{4})");
        extractAdditionalInformation(content, resumeSections, "Height", "(?i)\\b(height):?\\s*([0-9]+\\s*(cm|in|ft))");
        extractAdditionalInformation(content, resumeSections, "Health Status", "(?i)\\b(health\\s+status):?\\s*(.*)");
        extractAdditionalInformation(content, resumeSections, "Religion", "(?i)\\b(religion):?\\s*(.*)");
        extractSexOrGenderInformation(content, resumeSections, "Sex/Gender");
        extractEducationInformation(content, resumeSections, "Education");

        return resumeSections;
    }

    private static void extractEducationInformation(String content, Map<String, String> resumeSections, String key) {
        Pattern pattern = Pattern.compile("(?i)\\b(education(?:al)?\\s+(?:qualifications|background)|educational\\s+qualifications):?\\s*(.*)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String educationContent = matcher.group(2).trim();
            resumeSections.put(key, educationContent);
        }
    }

    private static void extractSkillsInformation(String content, Map<String, String> resumeSections, String key) {
        Pattern pattern = Pattern.compile("(?i)\\b(skills\\s*(&|and)\\s*abilities|skills):?\\s*(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String skillsContent = matcher.group(3).trim();
            resumeSections.put(key, skillsContent);
        }
    }

    private static void extractAdditionalInformation(String content, Map<String, String> resumeSections, String key, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            try {
                resumeSections.put(key, matcher.group(1).trim());
            } catch (IndexOutOfBoundsException e) {
                resumeSections.put(key, matcher.group().trim());
            }
        }
    }


    private static void extractLanguagesInformation(String content, Map<String, String> resumeSections, String key) {
        Pattern pattern = Pattern.compile("(?i)\\b(languages|language):?\\s*(.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String languagesContent = matcher.group(2).trim();
            Map<String, String> languages = parseLanguages(languagesContent);
            String languagesFormatted = languages.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining(", "));
            resumeSections.put(key, languagesFormatted);
        }
    }

    private static Map<String, String> parseLanguages(String languagesContent) {
        Map<String, String> languages = new LinkedHashMap<>();
        String[] languageEntries = languagesContent.split("(?<!\\w)(?=[A-Z][a-z]*:)");

        for (String entry : languageEntries) {
            String[] parts = entry.split(":", 2);
            if (parts.length == 2) {
                String language = parts[0].trim();
                String proficiency = parts[1].trim();
                languages.put(language, proficiency);
            }
        }
        return languages;
    }

    private static void extractSexOrGenderInformation(String content, Map<String, String> resumeSections, String key) {
        Pattern pattern = Pattern.compile("(?i)\\b(sex|gender):?\\s*(.*)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String sexOrGender = matcher.group(2).trim();
            resumeSections.put(key, sexOrGender);
        }
    }

    private static String cleanContent(String content) {
        return content.replaceAll("\\s+", " ").trim();
    }


}
