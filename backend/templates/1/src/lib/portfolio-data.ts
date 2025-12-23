// Portfolio data types based on schema
export interface PortfolioData {
  meta: {
    targetRole: string;
    createdAt: string;
    status: string;
  };
  personalInfo: {
    name: string;
    github: string | null;
    email: string;
    profileImage: string | null;
    confirmed: boolean;
  };
  workExperience: Array<{
    company: string;
    period: string;
    responsibility: string;
    position: string;
    links: string[] | null;
    confirmed: boolean;
  }>;
  education: {
    display: boolean;
    items: Array<{
      school: string;
      degree: string;
      major: string;
      period: string;
      confirmed: boolean;
    }>;
  };
  representativeProjects: Array<{
    name: string;
    period: string;
    responsibility: string[];
    techStack: string[];
    links: string[] | null;
    confirmed: boolean;
  }>;
  projects: Array<{
    name: string;
    period: string;
    responsibility: string[];
    techStack: string[];
    links: string[] | null;
    confirmed: boolean;
  }>;
  awards: Array<{
    title: string;
    period: string;
    content: string;
    organization: string;
    links: string[] | null;
    confirmed: boolean;
  }>;
  activities: {
    major: Array<{
      title: string;
      period: string;
      content: string;
      links: string[] | null;
      includeInResume: boolean;
      confirmed: boolean;
    }>;
    minor: Array<{
      title: string;
      period: string;
      content: string;
      links: string[] | null;
      includeInResume: boolean;
      confirmed: boolean;
    }>;
  };
  certifications: Array<{
    title: string;
    period: string;
    organization: string;
    links: string[] | null;
    confirmed: boolean;
  }>;
  technicalSkills: {
    strong: string[];
    knowledgeable: string[];
    confirmed: boolean;
  };
  about: {
    sentences: string[];
    confirmed: boolean;
  };
}

// Period format converter: "YYYYMMDD~YYYYMMDD" or "YYYYMMDD~present" → "Mar 2020 — Feb 2022"
export function formatPeriod(period: string): string {
  if (!period) return "";
  const [start, end] = period.split("~");

  const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

  const formatDate = (dateStr: string): string => {
    if (dateStr === "present") return "Present";
    if (dateStr.length >= 6) {
      const year = dateStr.slice(0, 4);
      const monthIndex = parseInt(dateStr.slice(4, 6), 10) - 1;
      return `${months[monthIndex]} ${year}`;
    }
    return dateStr;
  };

  return `${formatDate(start)} — ${formatDate(end)}`;
}

// Transform portfolio.json to component-friendly format
export function transformProfileData(portfolio: PortfolioData) {
  return {
    name: portfolio.personalInfo.name,
    title: portfolio.meta.targetRole,
    avatar: portfolio.personalInfo.profileImage || "/placeholder.svg",
    email: portfolio.personalInfo.email,
    github: portfolio.personalInfo.github,
  };
}

export function transformAboutData(portfolio: PortfolioData) {
  return {
    description: portfolio.about?.sentences || [],
  };
}

export function transformResumeData(portfolio: PortfolioData) {
  return {
    education: portfolio.education?.display !== false
      ? (portfolio.education?.items || []).map(edu => ({
          title: `${edu.school} - ${edu.degree}`,
          period: formatPeriod(edu.period),
          description: edu.major,
        }))
      : [],
    experience: (portfolio.workExperience || []).map(exp => ({
      title: `${exp.position} @ ${exp.company}`,
      period: formatPeriod(exp.period),
      description: exp.responsibility,
    })),
    skills: [
      ...(portfolio.technicalSkills?.strong || []).map(skill => ({
        name: skill,
        level: 90,
        category: 'strong' as const,
      })),
      ...(portfolio.technicalSkills?.knowledgeable || []).map(skill => ({
        name: skill,
        level: 70,
        category: 'knowledgeable' as const,
      })),
    ],
    awards: (portfolio.awards || []).map(award => ({
      title: award.title,
      period: formatPeriod(award.period),
      description: `${award.organization} - ${award.content}`,
      link: award.links?.[0] || null,
    })),
    certifications: (portfolio.certifications || []).map(cert => ({
      title: cert.title,
      period: formatPeriod(cert.period),
      organization: cert.organization,
      link: cert.links?.[0] || null,
    })),
    activities: (portfolio.activities?.major || [])
      .filter(act => act.includeInResume)
      .map(act => ({
        title: act.title,
        period: formatPeriod(act.period),
        content: act.content,
        link: act.links?.[0] || null,
      })),
  };
}

export function transformPortfolioData(portfolio: PortfolioData) {
  const allProjects = [
    ...(portfolio.representativeProjects || []),
    ...(portfolio.projects || []),
  ];

  return {
    projects: allProjects.map(project => ({
      title: project.name,
      description: Array.isArray(project.responsibility)
        ? project.responsibility.join(", ")
        : project.responsibility,
      tech: project.techStack || [],
      link: project.links?.[0] || null,
      period: formatPeriod(project.period),
    })),
  };
}

export function transformContactData(portfolio: PortfolioData) {
  return {
    email: portfolio.personalInfo.email,
    github: portfolio.personalInfo.github,
  };
}

// Fetch portfolio data
// Note: portfolio.json should be in public/ folder for Next.js
export async function fetchPortfolioData(): Promise<PortfolioData> {
  const response = await fetch('/portfolio.json');
  if (!response.ok) {
    throw new Error(`Failed to fetch portfolio data: ${response.status}`);
  }
  return response.json();
}
