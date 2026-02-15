package com.secureexam.backend.config;

import com.secureexam.backend.user.Role;
import com.secureexam.backend.user.User;
import com.secureexam.backend.user.repository.RoleRepository;
import com.secureexam.backend.user.repository.UserRepository;
import com.secureexam.backend.exam.repository.ExamRepository;
import com.secureexam.backend.question.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class TestDataInitializer {

        @Bean
        CommandLineRunner initTestData(
                        UserRepository userRepository,
                        RoleRepository roleRepository,
                        ExamRepository examRepository,
                        QuestionRepository questionRepository,
                        PasswordEncoder passwordEncoder) {

                return args -> {

                        // Create STUDENT role if not exists
                        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                                        .orElseGet(() -> roleRepository.save(new Role("ROLE_STUDENT")));

                        // Create ADMIN role if not exists
                        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                                        .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

                        // Check if student user exists
                        User student = userRepository
                                        .findByEmail("student@secureexam.com")
                                        .orElse(null);

                        // Create student user
                        if (student == null) {
                                student = new User();
                                student.setName("Test Student");
                                student.setEmail("student@secureexam.com");
                                student.setPassword(passwordEncoder.encode("student123"));
                                student.setEnabled(true);
                                student.setRoles(Set.of(studentRole));

                                userRepository.save(student);
                                System.out.println("Student user created!");
                        }

                        // Check if admin user exists
                        User admin = userRepository
                                        .findByEmail("admin@secureexam.com")
                                        .orElse(null);

                        // Create admin user
                        if (admin == null) {
                                admin = new User();
                                admin.setName("Admin User");
                                admin.setEmail("admin@secureexam.com");
                                admin.setPassword(passwordEncoder.encode("admin123"));
                                admin.setEnabled(true);
                                admin.setRoles(Set.of(adminRole));

                                userRepository.save(admin);
                                System.out.println("Admin user created!");
                        }

                        // Create Sample Exam
                        if (examRepository.count() == 0) {
                                com.secureexam.backend.exam.entity.Exam exam = new com.secureexam.backend.exam.entity.Exam();
                                exam.setTitle("General Knowledge Sample");
                                exam.setDescription("A sample exam to test the dashboard.");
                                exam.setDurationMinutes(60);
                                examRepository.save(exam);
                                System.out.println("Sample exam created!");

                                // Add Questions
                                if (questionRepository.count() == 0) {
                                        com.secureexam.backend.exam.entity.Question q1 = new com.secureexam.backend.exam.entity.Question();
                                        q1.setQuestionText("What is the capital of France?");
                                        q1.setOptionA("Berlin");
                                        q1.setOptionB("Madrid");
                                        q1.setOptionC("Paris");
                                        q1.setOptionD("Rome");
                                        q1.setCorrectAnswer("Paris");
                                        q1.setExam(exam);
                                        questionRepository.save(q1);

                                        com.secureexam.backend.exam.entity.Question q2 = new com.secureexam.backend.exam.entity.Question();
                                        q2.setQuestionText("Which planet is known as the Red Planet?");
                                        q2.setOptionA("Earth");
                                        q2.setOptionB("Mars");
                                        q2.setOptionC("Jupiter");
                                        q2.setOptionD("Venus");
                                        q2.setCorrectAnswer("Mars");
                                        q2.setExam(exam);
                                        questionRepository.save(q2);

                                        System.out.println("Sample questions created!");
                                }
                        }
                        // Create Top 50 Cybersecurity Questions Exam
                        createCyberSecurityExam(examRepository, questionRepository);
                };
        }

        private void createCyberSecurityExam(ExamRepository examRepository, QuestionRepository questionRepository) {
                if (examRepository.findByTitle("Top 50 Cybersecurity Questions").isEmpty()) {
                        com.secureexam.backend.exam.entity.Exam exam = new com.secureexam.backend.exam.entity.Exam();
                        exam.setTitle("Top 50 Cybersecurity Questions");
                        exam.setDescription(
                                        "A comprehensive exam covering top 50 cybersecurity concepts including Phishing, Malware, Encryption, Firewalls, and more.");
                        exam.setDurationMinutes(90);
                        exam.setCategory("PRACTICE");
                        examRepository.save(exam);
                        System.out.println("Cybersecurity exam created!");

                        // Add 50 Questions
                        addQuestion(questionRepository, exam, "What does CIA stand for in information security?",
                                        "Confidentiality, Integrity, Availability",
                                        "Control, Intelligence, Authorization",
                                        "Computer, Internet, Access", "Cyber, Information, Attack",
                                        "Confidentiality, Integrity, Availability");
                        addQuestion(questionRepository, exam, "Which of the following is considered a strong password?",
                                        "password123", "admin", "P@ssw0rd!", "12345678", "P@ssw0rd!");
                        addQuestion(questionRepository, exam, "What is Phishing?", "A type of fish",
                                        "A fraudulent attempt to obtain sensitive information", "A network protocol",
                                        "A firewall rule",
                                        "A fraudulent attempt to obtain sensitive information");
                        addQuestion(questionRepository, exam, "What is the purpose of a Firewall?",
                                        "To heat the server room",
                                        "To monitor and control network traffic", "To speed up the internet",
                                        "To store passwords",
                                        "To monitor and control network traffic");
                        addQuestion(questionRepository, exam, "What is Malware?", "Hardware malfunction",
                                        "Malicious software",
                                        "A network cable", "A secure website", "Malicious software");
                        addQuestion(questionRepository, exam,
                                        "Which protocol is used for secure communication over a computer network?",
                                        "HTTP", "FTP", "HTTPS",
                                        "Telnet", "HTTPS");
                        addQuestion(questionRepository, exam, "What is Social Engineering?", "Building bridges",
                                        "Manipulating people into performing actions or divulging confidential information",
                                        "Writing code",
                                        "Designing networks",
                                        "Manipulating people into performing actions or divulging confidential information");
                        addQuestion(questionRepository, exam, "What is Two-Factor Authentication (2FA)?",
                                        "Logging in twice",
                                        "Using two passwords",
                                        "A security process in which users provide two different authentication factors",
                                        "Sharing accounts",
                                        "A security process in which users provide two different authentication factors");
                        addQuestion(questionRepository, exam, "What is a DDoS attack?", "Direct Denial of Service",
                                        "Distributed Denial of Service", "Data Destruction of Service",
                                        "Digital Domain of Service",
                                        "Distributed Denial of Service");
                        addQuestion(questionRepository, exam, "What is Ransomware?", "Software that refunds money",
                                        "Malware that encrypts files and demands payment", "Free antivirus",
                                        "A backup tool",
                                        "Malware that encrypts files and demands payment");
                        addQuestion(questionRepository, exam, "What is Encryption?", "Deleting data",
                                        "Converting information into a code to prevent unauthorized access",
                                        "Compressing files",
                                        "Copying data",
                                        "Converting information into a code to prevent unauthorized access");
                        addQuestion(questionRepository, exam, "What is a VPN?", "Virtual Private Network",
                                        "Very Personal Network",
                                        "Visual Public Network", "Virtual Public Network", "Virtual Private Network");
                        addQuestion(questionRepository, exam, "What is SQL Injection?", "Cleaning the database",
                                        "Injecting code into a datastream to manipulate a database",
                                        "Optimizing SQL queries",
                                        "Backing up a database",
                                        "Injecting code into a datastream to manipulate a database");
                        addQuestion(questionRepository, exam, "What is Cross-Site Scripting (XSS)?",
                                        "Crossing the road",
                                        "Injecting malicious scripts into trusted websites", "Designing web pages",
                                        "Secure coding practice", "Injecting malicious scripts into trusted websites");
                        addQuestion(questionRepository, exam, "What is a Botnet?", "A robot network",
                                        "A network of compromised computers controlled by a central server",
                                        "A fast internet connection",
                                        "A social media platform",
                                        "A network of compromised computers controlled by a central server");
                        addQuestion(questionRepository, exam, "What is Spyware?", "Software for spies",
                                        "Malware that gathers information about a person or organization", "A game",
                                        "An operating system",
                                        "Malware that gathers information about a person or organization");
                        addQuestion(questionRepository, exam, "What is a Zero-Day Vulnerability?",
                                        "A vulnerability fixed on day zero",
                                        "A flaw that is unknown to the software vendor",
                                        "A scheduled update", "A minor bug",
                                        "A flaw that is unknown to the software vendor");
                        addQuestion(questionRepository, exam, "What is a Keylogger?", "A tool to make keys",
                                        "Software that records keystrokes", "A password manager", "A locking mechanism",
                                        "Software that records keystrokes");
                        addQuestion(questionRepository, exam, "What is Man-in-the-Middle (MitM) attack?",
                                        "A referee in a game",
                                        "An attacker intercepting communication between two parties",
                                        "A middle management issue",
                                        "A server error", "An attacker intercepting communication between two parties");
                        addQuestion(questionRepository, exam, "What is Hashing?", "Chopping vegetables",
                                        "Providing a mapping between an arbitrary length input and a fixed length output",
                                        "Encrypting data", "Compressing files",
                                        "Providing a mapping between an arbitrary length input and a fixed length output");
                        addQuestion(questionRepository, exam, "What is Salt in cryptography?", "Sodium chloride",
                                        "Random data added to a password before hashing", "A secret key",
                                        "An encryption algorithm",
                                        "Random data added to a password before hashing");
                        addQuestion(questionRepository, exam, "What is a Digital Certificate?", "A diploma",
                                        "An electronic document used to prove the ownership of a public key",
                                        "A receipt", "A license",
                                        "An electronic document used to prove the ownership of a public key");
                        addQuestion(questionRepository, exam, "What is PKI?", "Public Key Infrastructure",
                                        "Private Key Infrastructure", "Personal Key Information",
                                        "Public Key Information",
                                        "Public Key Infrastructure");
                        addQuestion(questionRepository, exam, "What is a Security Patch?", "A piece of cloth",
                                        "A software update to fix vulnerabilities", "A password reset",
                                        "A firewall rule",
                                        "A software update to fix vulnerabilities");
                        addQuestion(questionRepository, exam, "What is Biometric Authentication?",
                                        "Using biological traits for identification", "Using a password",
                                        "Using a smart card",
                                        "Using a token", "Using biological traits for identification");
                        addQuestion(questionRepository, exam, "What is a Trojan Horse?", "A wooden horse",
                                        "Malware disguised as legitimate software", "A virus", "A worm",
                                        "Malware disguised as legitimate software");
                        addQuestion(questionRepository, exam, "What is a Worm?", "An insect",
                                        "Self-replicating malware that spreads across networks", "A file virus",
                                        "A macro virus",
                                        "Self-replicating malware that spreads across networks");
                        addQuestion(questionRepository, exam, "What is Brute Force Attack?", "Physical violence",
                                        "Trial and error method to guess passwords", "Social engineering", "Phishing",
                                        "Trial and error method to guess passwords");
                        addQuestion(questionRepository, exam, "What is Dictionary Attack?", "Throwing a dictionary",
                                        "Attempting to guess a password using a list of common words",
                                        "Searching for definitions",
                                        "Learning a language",
                                        "Attempting to guess a password using a list of common words");
                        addQuestion(questionRepository, exam, "What is Steganography?", "Writing backward",
                                        "Hiding data within other data", "Encrypting data", "Compressing data",
                                        "Hiding data within other data");
                        addQuestion(questionRepository, exam, "What is a Honeypot?", "A pot of honey",
                                        "A decoy system to attract attackers", "A secure server", "A firewall",
                                        "A decoy system to attract attackers");
                        addQuestion(questionRepository, exam, "What is Penetration Testing?", "Testing pens",
                                        "Simulated cyber attack to find vulnerabilities", "Checking network speed",
                                        " installing software",
                                        "Simulated cyber attack to find vulnerabilities");
                        addQuestion(questionRepository, exam, "What is Vulnerability Scanning?", "Scanning documents",
                                        "Automated process to identify security weaknesses", "Reading emails",
                                        "Checking for viruses",
                                        "Automated process to identify security weaknesses");
                        addQuestion(questionRepository, exam, "What is Incident Response?", "Answering the phone",
                                        "An organized approach to addressing and managing the aftermath of a security breach",
                                        "Reporting bugs", "Writing logs",
                                        "An organized approach to addressing and managing the aftermath of a security breach");
                        addQuestion(questionRepository, exam, "What is Forensics in Cyber Security?",
                                        "Watching crime shows",
                                        "Investigation and analysis of digital evidence", "Writing reports",
                                        "Monitoring traffic",
                                        "Investigation and analysis of digital evidence");
                        addQuestion(questionRepository, exam, "What is Data Loss Prevention (DLP)?", "Backing up data",
                                        "Strategy to ensure sensitive data is not lost or misused", "Deleting data",
                                        "Encrypting data",
                                        "Strategy to ensure sensitive data is not lost or misused");
                        addQuestion(questionRepository, exam, "What is IAM?", "I am a robot",
                                        "Identity and Access Management",
                                        "Internet Access Method", "Internal Audit Mechanism",
                                        "Identity and Access Management");
                        addQuestion(questionRepository, exam, "What is RBAC?", "Role-Based Access Control",
                                        "Rule-Based Access Control", "Remote Backup and Control",
                                        "Real-Time Access Control",
                                        "Role-Based Access Control");
                        addQuestion(questionRepository, exam, "What is Least Privilege Principle?",
                                        "Giving everyone admin access",
                                        "Giving users only the permissions they need", "Restricting all access",
                                        "None of the above",
                                        "Giving users only the permissions they need");
                        addQuestion(questionRepository, exam, "What is a Sandbox?", "A place for kids to play",
                                        "An isolated environment for running programs", "A beach", "A desert",
                                        "An isolated environment for running programs");
                        addQuestion(questionRepository, exam, "What is BYOD?", "Bring Your Own Device",
                                        "Buy Your Own Data",
                                        "Bring Your Own Disk", "Build Your Own Database", "Bring Your Own Device");
                        addQuestion(questionRepository, exam, "What is IoT Security?", "Internet of Things Security",
                                        "Internal office Technology", "Input output Testing", "Internet of Technology",
                                        "Internet of Things Security");
                        addQuestion(questionRepository, exam, "What is Cloud Security?", "Securing the sky",
                                        "Protection of data stored online via cloud computing platforms",
                                        "Weather forecasting",
                                        "Airplane safety",
                                        "Protection of data stored online via cloud computing platforms");
                        addQuestion(questionRepository, exam, "What is a Rootkit?", "A gardening tool",
                                        "Malware designed to gain unauthorized root access", "A linux command",
                                        "A file system",
                                        "Malware designed to gain unauthorized root access");
                        addQuestion(questionRepository, exam, "What is Sniffing?", "Smelling something",
                                        "Monitoring and capturing data packets", "Sneezing", "Deleting files",
                                        "Monitoring and capturing data packets");
                        addQuestion(questionRepository, exam, "What is Spoofing?", "Making fun of someone",
                                        "Masquerading as another device or user", "Spilling a drink", "Speeding up",
                                        "Masquerading as another device or user");
                        addQuestion(questionRepository, exam, "What is HTTPS based on?", "SSL/TLS", "SSH", "Telnet",
                                        "FTP",
                                        "SSL/TLS");
                        addQuestion(questionRepository, exam, "Which is more secure, HTTP or HTTPS?", "HTTP", "HTTPS",
                                        "Both are same", "Neither", "HTTPS");
                        addQuestion(questionRepository, exam, "What length of password is generally recommended?",
                                        "4 characters",
                                        "8 characters", "12+ characters", "1 character", "12+ characters");
                        addQuestion(questionRepository, exam, "Why should you update your software?",
                                        "To get new features only",
                                        "To patch security vulnerabilities", "To use more space",
                                        "To slow down computer",
                                        "To patch security vulnerabilities");

                        System.out.println("50 Cybersecurity questions added!");
                }
        }

        private void addQuestion(QuestionRepository questionRepository, com.secureexam.backend.exam.entity.Exam exam,
                        String text, String a, String b, String c, String d, String ans) {
                com.secureexam.backend.exam.entity.Question q = new com.secureexam.backend.exam.entity.Question();
                q.setQuestionText(text);
                q.setOptionA(a);
                q.setOptionB(b);
                q.setOptionC(c);
                q.setOptionD(d);
                q.setCorrectAnswer(ans);
                q.setExam(exam);
                questionRepository.save(q);
        }
}