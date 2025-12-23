// Period format converter: "YYYYMMDD~YYYYMMDD" or "YYYYMMDD~present" → "YYYY.MM ~ YYYY.MM"
const formatPeriod = (period) => {
    if (!period) return "";
    const [start, end] = period.split("~");
    const formatDate = (dateStr) => {
        if (dateStr === "present") return "Present";
        if (dateStr.length >= 6) {
            return `${dateStr.slice(0, 4)}.${dateStr.slice(4, 6)}`;
        }
        return dateStr;
    };
    return `${formatDate(start)} ~ ${formatDate(end)}`;
};

// Transform portfolio.json to siteContent format
const transformToSiteContent = (portfolio) => {
    const navLinks = [
        { label: "Projects", href: "#projects" },
        { label: "About", href: "#about" },
        { label: "Tech", href: "#Tech" }
    ];

    // Add optional sections to nav
    if (portfolio.workExperience && portfolio.workExperience.length > 0) {
        navLinks.push({ label: "Experience", href: "#experience" });
    }
    if (portfolio.education && portfolio.education.display && portfolio.education.items && portfolio.education.items.length > 0) {
        navLinks.push({ label: "Education", href: "#education" });
    }
    if (portfolio.awards && portfolio.awards.length > 0) {
        navLinks.push({ label: "Awards", href: "#awards" });
    }

    // Add GitHub link if exists
    if (portfolio.personalInfo.github) {
        navLinks.push({
            iconClass: "fab fa-github",
            href: portfolio.personalInfo.github,
            external: true,
            noUnderline: true
        });
    }

    // Combine representative projects and projects
    const allProjects = [
        ...(portfolio.representativeProjects || []),
        ...(portfolio.projects || [])
    ];

    return {
        navigation: {
            logo: portfolio.personalInfo.name,
            logoAccent: "_",
            links: navLinks
        },
        hero: {
            intro: {
                text: "Hi I'm",
                link: portfolio.personalInfo.github
                    ? { label: portfolio.personalInfo.name, href: portfolio.personalInfo.github }
                    : null
            },
            subtitle: portfolio.meta.targetRole
        },
        projects: allProjects.map((p, index) => ({
            title: p.name,
            description: Array.isArray(p.responsibility)
                ? p.responsibility.join(", ")
                : (p.responsibility || ""),
            url: (p.links && p.links.length > 0) ? p.links[0] : "#",
            backgroundImage: null, // User needs to add images
            featured: index === 0 && allProjects.length > 1 // First project is featured
        })),
        about: {
            title: "About",
            paragraphs: portfolio.about ? portfolio.about.sentences : []
        },
        tech: {
            title: "Tech",
            categories: [
                {
                    label: "Strong",
                    items: portfolio.technicalSkills && portfolio.technicalSkills.strong
                        ? portfolio.technicalSkills.strong.join(" / ")
                        : ""
                },
                {
                    label: "Knowledgeable",
                    items: portfolio.technicalSkills && portfolio.technicalSkills.knowledgeable
                        ? portfolio.technicalSkills.knowledgeable.join(" / ")
                        : ""
                }
            ].filter(cat => cat.items.length > 0)
        },
        experience: {
            title: "Experience",
            items: (portfolio.workExperience || []).map(exp => ({
                position: exp.position,
                company: exp.company,
                period: formatPeriod(exp.period),
                responsibility: exp.responsibility
            }))
        },
        education: {
            title: "Education",
            display: portfolio.education ? portfolio.education.display : false,
            items: (portfolio.education && portfolio.education.items) ? portfolio.education.items.map(edu => ({
                school: edu.school,
                degree: edu.degree,
                major: edu.major,
                period: formatPeriod(edu.period)
            })) : []
        },
        awards: {
            title: "Awards",
            items: (portfolio.awards || []).map(award => ({
                label: award.title,
                detail: `${award.organization} / ${award.content}`,
                period: formatPeriod(award.period),
                link: (award.links && award.links.length > 0)
                    ? { href: award.links[0], text: "link" }
                    : null
            }))
        },
        certifications: {
            title: "Certifications",
            items: (portfolio.certifications || []).map(cert => ({
                title: cert.title,
                organization: cert.organization,
                period: formatPeriod(cert.period),
                link: (cert.links && cert.links.length > 0)
                    ? { href: cert.links[0], text: "link" }
                    : null
            }))
        },
        activities: {
            title: "Activities",
            items: (portfolio.activities && portfolio.activities.major)
                ? portfolio.activities.major.filter(a => a.includeInResume).map(act => ({
                    title: act.title,
                    content: act.content,
                    period: formatPeriod(act.period),
                    link: (act.links && act.links.length > 0)
                        ? { href: act.links[0], text: "link" }
                        : null
                }))
                : []
        }
    };
};

const addClassOnScroll = (element) => element && element.classList.add("come-in");

const renderNavigation = (navData) => {
    const navRoot = document.getElementById("site-nav");
    if (!navRoot || !navData) return;

    const logoAccent = navData.logoAccent || "";
    const linksMarkup = (navData.links || [])
        .map((item) => {
            const classes = [
                item.noUnderline ? "no-link" : "",
                item.iconClass ? "no-link" : ""
            ]
                .filter(Boolean)
                .join(" ");
            const content = item.iconClass ? `<i class="${item.iconClass}"></i>` : item.label;
            const target = item.external ? ' target="_blank" rel="noopener noreferrer"' : "";
            return `<li><a href="${item.href}" class="${classes.trim()}"${target}>${content}</a></li>`;
        })
        .join("");

    navRoot.innerHTML = `
        <div class="nav-wrapper">
            <h1 class="nav-logo">${navData.logo}<span class="logo-end">${logoAccent}</span></h1>
            <ul class="nav-menu">${linksMarkup}</ul>
        </div>
    `;
};

const renderHero = (heroData) => {
    const heroRoot = document.getElementById("hero");
    if (!heroRoot || !heroData) return;

    let introMarkup = "";
    if (heroData.intro) {
        if (heroData.intro.link) {
            introMarkup = `<h3>${heroData.intro.text || ""} <a href="${heroData.intro.link.href}" target="_blank">${heroData.intro.link.label}</a></h3>`;
        } else {
            introMarkup = `<h3>${heroData.intro.text || ""} ${heroData.intro.link?.label || ""}</h3>`;
        }
    }
    const subtitleMarkup = heroData.subtitle ? `<h3>${heroData.subtitle}</h3>` : "";

    heroRoot.innerHTML = `${introMarkup}${subtitleMarkup}`;
};

const createProjectItem = (project) => {
    const listItem = document.createElement("li");
    const classNames = ["project", "ripple", "load-bg"];
    if (project.featured) classNames.push("project--featured");
    listItem.className = classNames.join(" ");

    const image = document.createElement("div");
    image.className = "project-image";
    if (project.backgroundImage) {
        image.style.backgroundImage = `url(${project.backgroundImage})`;
    } else {
        // Default gradient background if no image
        listItem.style.backgroundColor = "#2d2d2d";
    }

    const link = document.createElement("a");
    link.className = "project-content";
    link.href = project.url;
    if (project.url && project.url !== "#") {
        link.target = "_blank";
        link.rel = "noopener noreferrer";
    }

    const title = document.createElement("h3");
    title.className = "project-title";
    title.innerHTML = `<span>${project.title}</span>`;

    const underline = document.createElement("span");
    underline.className = "project-underline";

    const description = document.createElement("div");
    description.className = "project-description";
    description.innerHTML = `<span>${project.description}</span>`;

    link.appendChild(title);
    link.appendChild(underline);
    link.appendChild(description);
    listItem.appendChild(image);
    listItem.appendChild(link);

    return listItem;
};

const renderProjects = (projects) => {
    const projectsRoot = document.getElementById("projects");
    if (!projectsRoot || !projects || !projects.length) {
        if (projectsRoot) projectsRoot.style.display = "none";
        return;
    }
    projectsRoot.innerHTML = "";
    const fragment = document.createDocumentFragment();
    projects.forEach((project) => fragment.appendChild(createProjectItem(project)));
    projectsRoot.appendChild(fragment);
};

const buildSectionWrapper = (sectionData) => {
    const wrapper = document.createElement("div");
    wrapper.className = "text-wrap animate-text";
    if (sectionData.title) {
        const heading = document.createElement("h2");
        heading.innerHTML = `${sectionData.title}<span class="logo-end">_</span>`;
        wrapper.appendChild(heading);
    }
    return wrapper;
};

const renderAbout = (aboutData) => {
    const aboutRoot = document.getElementById("about");
    if (!aboutRoot || !aboutData || !aboutData.paragraphs || aboutData.paragraphs.length === 0) {
        if (aboutRoot) aboutRoot.style.display = "none";
        return;
    }
    const wrapper = buildSectionWrapper(aboutData);
    aboutData.paragraphs.forEach((text) => {
        const paragraph = document.createElement("p");
        paragraph.className = "text-intro";
        paragraph.textContent = text;
        wrapper.appendChild(paragraph);
    });
    aboutRoot.innerHTML = "";
    aboutRoot.appendChild(wrapper);
};

const renderTech = (techData) => {
    const techRoot = document.getElementById("Tech");
    if (!techRoot || !techData || !techData.categories || techData.categories.length === 0) {
        if (techRoot) techRoot.style.display = "none";
        return;
    }
    const wrapper = buildSectionWrapper(techData);
    techData.categories.forEach((category) => {
        const label = document.createElement("p");
        label.className = "text-intro";
        label.textContent = category.label;
        const items = document.createElement("p");
        items.textContent = category.items;
        wrapper.appendChild(label);
        wrapper.appendChild(items);
    });
    techRoot.innerHTML = "";
    techRoot.appendChild(wrapper);
};

const renderExperience = (expData) => {
    const expRoot = document.getElementById("experience");
    if (!expRoot || !expData || !expData.items || expData.items.length === 0) {
        if (expRoot) expRoot.style.display = "none";
        return;
    }
    const wrapper = buildSectionWrapper(expData);
    expData.items.forEach((exp) => {
        const position = document.createElement("p");
        position.className = "text-intro";
        position.textContent = `${exp.position} @ ${exp.company}`;

        const detail = document.createElement("p");
        detail.innerHTML = `${exp.period}<br>${exp.responsibility}`;

        wrapper.appendChild(position);
        wrapper.appendChild(detail);
    });
    expRoot.innerHTML = "";
    expRoot.appendChild(wrapper);
};

const renderEducation = (eduData) => {
    const eduRoot = document.getElementById("education");
    if (!eduRoot || !eduData || !eduData.display || !eduData.items || eduData.items.length === 0) {
        if (eduRoot) eduRoot.style.display = "none";
        return;
    }
    const wrapper = buildSectionWrapper(eduData);
    eduData.items.forEach((edu) => {
        const title = document.createElement("p");
        title.className = "text-intro";
        title.textContent = `${edu.school} - ${edu.degree}`;

        const detail = document.createElement("p");
        detail.textContent = `${edu.major} (${edu.period})`;

        wrapper.appendChild(title);
        wrapper.appendChild(detail);
    });
    eduRoot.innerHTML = "";
    eduRoot.appendChild(wrapper);
};

const renderAwards = (awardsData) => {
    const awardsRoot = document.getElementById("awards");
    if (!awardsRoot || !awardsData || !awardsData.items || awardsData.items.length === 0) {
        if (awardsRoot) awardsRoot.style.display = "none";
        return;
    }
    const wrapper = buildSectionWrapper(awardsData);
    awardsData.items.forEach((award) => {
        const title = document.createElement("p");
        title.className = "text-intro";
        title.textContent = award.label;

        const detail = document.createElement("p");
        let detailHTML = `${award.period} / ${award.detail}`;
        if (award.link) {
            detailHTML += ` <a href="${award.link.href}" target="_blank">${award.link.text}</a>`;
        }
        detail.innerHTML = detailHTML;

        wrapper.appendChild(title);
        wrapper.appendChild(detail);
    });
    awardsRoot.innerHTML = "";
    awardsRoot.appendChild(wrapper);
};

const renderCertifications = (certData) => {
    const certRoot = document.getElementById("certifications");
    if (!certRoot || !certData || !certData.items || certData.items.length === 0) {
        if (certRoot) certRoot.style.display = "none";
        return;
    }
    const wrapper = buildSectionWrapper(certData);
    certData.items.forEach((cert) => {
        const title = document.createElement("p");
        title.className = "text-intro";
        title.textContent = cert.title;

        const detail = document.createElement("p");
        let detailHTML = `${cert.organization} (${cert.period})`;
        if (cert.link) {
            detailHTML += ` <a href="${cert.link.href}" target="_blank">${cert.link.text}</a>`;
        }
        detail.innerHTML = detailHTML;

        wrapper.appendChild(title);
        wrapper.appendChild(detail);
    });
    certRoot.innerHTML = "";
    certRoot.appendChild(wrapper);
};

const renderActivities = (actData) => {
    const actRoot = document.getElementById("activities");
    if (!actRoot || !actData || !actData.items || actData.items.length === 0) {
        if (actRoot) actRoot.style.display = "none";
        return;
    }
    const wrapper = buildSectionWrapper(actData);
    actData.items.forEach((act) => {
        const title = document.createElement("p");
        title.className = "text-intro";
        title.textContent = act.title;

        const detail = document.createElement("p");
        let detailHTML = `${act.period} / ${act.content}`;
        if (act.link) {
            detailHTML += ` <a href="${act.link.href}" target="_blank">${act.link.text}</a>`;
        }
        detail.innerHTML = detailHTML;

        wrapper.appendChild(title);
        wrapper.appendChild(detail);
    });
    actRoot.innerHTML = "";
    actRoot.appendChild(wrapper);
};

const initScrollAnimations = () => {
    const sections = document.querySelectorAll(".animate-text");

    const handleScroll = () => {
        const scrollPos = window.scrollY + window.innerHeight;
        sections.forEach((section) => {
            if (section.offsetTop < scrollPos - 100) {
                addClassOnScroll(section);
            }
        });
    };

    window.addEventListener("scroll", handleScroll);
    handleScroll();
};

const renderSite = (data) => {
    renderNavigation(data.navigation);
    renderHero(data.hero);
    renderProjects(data.projects);
    renderAbout(data.about);
    renderTech(data.tech);
    renderExperience(data.experience);
    renderEducation(data.education);
    renderAwards(data.awards);
    renderCertifications(data.certifications);
    renderActivities(data.activities);
    initScrollAnimations();
};

// Load portfolio data and render
window.addEventListener("DOMContentLoaded", async () => {
    try {
        const response = await fetch('./public/portfolio.json');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const portfolio = await response.json();
        const siteContent = transformToSiteContent(portfolio);
        renderSite(siteContent);
    } catch (error) {
        console.error("Failed to load portfolio data:", error);
        // Show error message
        const heroRoot = document.getElementById("hero");
        if (heroRoot) {
            heroRoot.innerHTML = `
                <h3>포트폴리오 데이터를 불러올 수 없습니다.</h3>
                <p style="color: #999; font-size: 0.9em;">./public/portfolio.json 파일이 존재하는지 확인해주세요.</p>
            `;
        }
    }
});
