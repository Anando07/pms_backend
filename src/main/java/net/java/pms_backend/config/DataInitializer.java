package net.java.pms_backend.config;

import lombok.AllArgsConstructor;
import net.java.pms_backend.entity.Ministry;
import net.java.pms_backend.entity.Passcode;
import net.java.pms_backend.entity.Role;
import net.java.pms_backend.entity.User;
import net.java.pms_backend.repository.MinistryRepository;
import net.java.pms_backend.repository.RoleRepository;
import net.java.pms_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MinistryRepository ministryRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedMinistries();
        seedRoles();
        seedAdminUser();
    }

    private void seedMinistries() {
        List<String> defaultMinistries = Arrays.asList(

                "President's Office",
                "Prime Minister's Office",
                "Cabinet Division",
                "Armed Forces Division",
                "Legislative and Parliamentary Affairs Division",

                "Ministry of Public Administration",
                "Ministry of Finance",
                "Finance Division",
                "Internal Resources Division",
                "Financial Institutions Division",
                "Economic Relations Division",

                "Ministry of Planning",
                "Planning Division",
                "Implementation Monitoring and Evaluation Division (IMED)",
                "Statistics and Informatics Division",

                "Ministry of Education",
                "Secondary and Higher Education Division",
                "Technical and Madrasah Education Division",

                "Ministry of Primary and Mass Education",

                "Ministry of Road Transport and Bridges",
                "Road Transport and Highways Division",
                "Bridges Division",

                "Ministry of Railways",

                "Ministry of Shipping",

                "Ministry of Civil Aviation and Tourism",

                "Ministry of Home Affairs",
                "Public Security Division",
                "Security Services Division",

                "Ministry of Foreign Affairs",

                "Ministry of Defence",

                "Ministry of Law, Justice and Parliamentary Affairs",
                "Legislative and Parliamentary Affairs Division",

                "Ministry of Agriculture",

                "Ministry of Fisheries and Livestock",

                "Ministry of Food",

                "Ministry of Commerce",

                "Ministry of Industries",

                "Ministry of Textiles and Jute",

                "Ministry of Information and Broadcasting",

                "Ministry of Information and Communication Technology",
                "Information and Communication Technology Division",

                "Ministry of Posts, Telecommunications and Information Technology",
                "Posts and Telecommunications Division",

                "Ministry of Science and Technology",

                "Ministry of Power, Energy and Mineral Resources",
                "Power Division",
                "Energy and Mineral Resources Division",

                "Ministry of Water Resources",

                "Ministry of Environment, Forest and Climate Change",

                "Ministry of Disaster Management and Relief",

                "Ministry of Local Government, Rural Development and Cooperatives",
                "Local Government Division",
                "Rural Development and Cooperatives Division",

                "Ministry of Housing and Public Works",

                "Ministry of Health and Family Welfare",
                "Health Services Division",
                "Medical Education and Family Welfare Division",

                "Ministry of Social Welfare",

                "Ministry of Women and Children Affairs",

                "Ministry of Youth and Sports",

                "Ministry of Cultural Affairs",

                "Ministry of Religious Affairs",

                "Ministry of Labour and Employment",

                "Ministry of Expatriates' Welfare and Overseas Employment",

                "Ministry of Liberation War Affairs",

                "Ministry of Chittagong Hill Tracts Affairs",

                "Ministry of Land",

                "Ministry of Foreign Affairs",

                "Ministry of Public Works",

                "Ministry of Jute and Textiles",

                "Ministry of Environment",

                "Ministry of Forest",

                "Election Commission Secretariat"
        );

        for (String ministryName : defaultMinistries) {
            if (ministryRepository.findByMinName(ministryName).isEmpty()) {
                Ministry ministry = Ministry.builder()
                        .minName(ministryName)
                        .build();
                ministryRepository.save(ministry);
            }
        }
    }

    private void seedRoles() {
        List<String> defaultRoles = Arrays.asList(
                "Super Admin",
                "Admin",
                "Project Director",
                "Assistant Project Director",
                "Project Officer",
                "Entry User",
                "Visitor"
        );

        for (String roleName : defaultRoles) {
            if (roleRepository.findByRoleName(roleName).isEmpty()) {
                Role role = Role.builder()
                        .roleName(roleName)
                        .build();
                roleRepository.save(role);
            }
        }
    }

    private void seedAdminUser() {
        String adminEmail = "irdbd08@gmail.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            Role superAdminRole = roleRepository.findByRoleName("Super Admin")
                    .orElseThrow(() -> new RuntimeException("Super Admin role not found"));

            // 1. Build the User entity without password
            User adminUser = User.builder()
                    .name("System Administrator")
                    .designation("Senior Systems Specialist")
                    .officeName("IRD Central Office")
                    .email(adminEmail)
                    .number("01700000000")
                    .minDiv("Internal Resources Division")
                    .active(true)
                    .role(superAdminRole)
                    .build();

            // 2. Build the associated Passcode entity
            Passcode passcode = Passcode.builder()
                    .passcode(passwordEncoder.encode("Ird@615ict"))
                    .active(true)
                    .expiresAt(LocalDateTime.now().plusYears(50)) // Set desired passcode expiry
                    .user(adminUser)
                    .build();

            // 3. Link the passcode to user (CascadeType.ALL will save passcode automatically)
            adminUser.setPasscode(passcode);

            userRepository.save(adminUser);
        }
    }
}