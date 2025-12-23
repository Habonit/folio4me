'use client'

import { useState, useEffect } from 'react'
import { ProfileSidebar } from '@/components/profile-sidebar'
import { AboutSection } from '@/components/about-section'
import { ResumeSection } from '@/components/resume-section'
import { PortfolioSection } from '@/components/portfolio-section'
import { ContactSection } from '@/components/contact-section-new'
import { ThemeToggle } from '@/components/theme-toggle'
import {
  PortfolioData,
  fetchPortfolioData,
  transformProfileData,
  transformAboutData,
  transformResumeData,
  transformPortfolioData,
  transformContactData,
} from '@/lib/portfolio-data'

export default function Home() {
  const [activeSection, setActiveSection] = useState('about')
  const [portfolio, setPortfolio] = useState<PortfolioData | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetchPortfolioData()
      .then(data => {
        setPortfolio(data)
        setLoading(false)
      })
      .catch(err => {
        setError(err.message)
        setLoading(false)
      })
  }, [])

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <p className="text-muted-foreground">Loading...</p>
      </div>
    )
  }

  if (error || !portfolio) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center flex-col gap-4">
        <p className="text-foreground text-xl">포트폴리오 데이터를 불러올 수 없습니다.</p>
        <p className="text-muted-foreground">../portfolio.json 파일이 존재하는지 확인해주세요.</p>
      </div>
    )
  }

  const profileData = transformProfileData(portfolio)
  const aboutData = transformAboutData(portfolio)
  const resumeData = transformResumeData(portfolio)
  const portfolioData = transformPortfolioData(portfolio)
  const contactData = transformContactData(portfolio)

  // Build navigation tabs based on available data
  const sections: string[] = ['about']

  if (resumeData.experience.length > 0 || resumeData.education.length > 0 || resumeData.skills.length > 0) {
    sections.push('resume')
  }

  if (portfolioData.projects.length > 0) {
    sections.push('portfolio')
  }

  sections.push('contact')

  return (
    <div className="min-h-screen bg-background p-3 sm:p-4 md:p-6 lg:p-12">
      <div className="fixed top-4 right-4 md:top-6 md:right-6 z-50">
        <ThemeToggle />
      </div>

      <div className="mx-auto max-w-7xl">
        <div className="flex flex-col lg:flex-row gap-3 sm:gap-4 md:gap-6">
          <ProfileSidebar data={profileData} />

          {/* Main Content */}
          <main className="flex-1 bg-card rounded-xl md:rounded-2xl border border-border overflow-hidden">
            {/* Navigation */}
            <nav className="flex gap-1 sm:gap-2 md:gap-4 p-3 sm:p-4 md:p-6 border-b border-border overflow-x-auto scrollbar-hide">
              {sections.map((section) => (
                <button
                  key={section}
                  onClick={() => setActiveSection(section)}
                  className={`px-3 sm:px-4 py-2 rounded-lg text-xs sm:text-sm font-medium capitalize transition-colors whitespace-nowrap flex-shrink-0 ${
                    activeSection === section
                      ? 'text-foreground bg-accent/10'
                      : 'text-muted-foreground hover:text-foreground hover:bg-secondary'
                  }`}
                >
                  {section}
                </button>
              ))}
            </nav>

            <div className="p-4 sm:p-5 md:p-6 lg:p-8">
              {activeSection === 'about' && <AboutSection data={aboutData} />}
              {activeSection === 'resume' && <ResumeSection data={resumeData} />}
              {activeSection === 'portfolio' && <PortfolioSection data={portfolioData} />}
              {activeSection === 'contact' && <ContactSection data={contactData} />}
            </div>
          </main>
        </div>
      </div>
    </div>
  )
}
