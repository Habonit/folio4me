import { BookOpen, Briefcase, Award, Medal, Activity, ExternalLink } from 'lucide-react'

interface ResumeItem {
  title: string
  period: string
  description: string
}

interface SkillItem {
  name: string
  level: number
  category: 'strong' | 'knowledgeable'
}

interface AwardItem {
  title: string
  period: string
  description: string
  link: string | null
}

interface CertificationItem {
  title: string
  period: string
  organization: string
  link: string | null
}

interface ActivityItem {
  title: string
  period: string
  content: string
  link: string | null
}

interface ResumeData {
  education: ResumeItem[]
  experience: ResumeItem[]
  skills: SkillItem[]
  awards: AwardItem[]
  certifications: CertificationItem[]
  activities: ActivityItem[]
}

interface ResumeSectionProps {
  data: ResumeData
}

export function ResumeSection({ data }: ResumeSectionProps) {
  return (
    <div className="space-y-8 md:space-y-10">
      <div>
        <h2 className="text-2xl md:text-3xl font-bold text-foreground mb-4">Resume</h2>
        <div className="w-10 h-1 bg-accent rounded-full mb-6" />
      </div>

      {/* Education */}
      {data.education.length > 0 && (
        <div>
          <div className="flex items-center gap-2 md:gap-3 mb-6">
            <BookOpen className="w-5 h-5 md:w-6 md:h-6 text-accent" />
            <h3 className="text-xl md:text-2xl font-bold text-foreground">Education</h3>
          </div>
          <div className="space-y-4">
            {data.education.map((item, index) => (
              <div key={index} className="relative pl-5 md:pl-6 pb-6 border-l-2 border-border last:pb-0">
                <div className="absolute -left-[9px] top-0 w-4 h-4 rounded-full bg-accent" />
                <h4 className="text-base md:text-lg font-semibold text-foreground mb-2">{item.title}</h4>
                <p className="text-xs md:text-sm text-accent mb-2">{item.period}</p>
                <p className="text-xs md:text-sm text-muted-foreground leading-relaxed">{item.description}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Experience */}
      {data.experience.length > 0 && (
        <div>
          <div className="flex items-center gap-2 md:gap-3 mb-6">
            <Briefcase className="w-5 h-5 md:w-6 md:h-6 text-accent" />
            <h3 className="text-xl md:text-2xl font-bold text-foreground">Experience</h3>
          </div>
          <div className="space-y-4">
            {data.experience.map((item, index) => (
              <div key={index} className="relative pl-5 md:pl-6 pb-6 border-l-2 border-border last:pb-0">
                <div className="absolute -left-[9px] top-0 w-4 h-4 rounded-full bg-accent" />
                <h4 className="text-base md:text-lg font-semibold text-foreground mb-2">{item.title}</h4>
                <p className="text-xs md:text-sm text-accent mb-2">{item.period}</p>
                <p className="text-xs md:text-sm text-muted-foreground leading-relaxed">{item.description}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Skills */}
      {data.skills.length > 0 && (
        <div>
          <h3 className="text-xl md:text-2xl font-bold text-foreground mb-6">My Skills</h3>
          <div className="space-y-5 md:space-y-6">
            {data.skills.map((skill, index) => (
              <div key={index}>
                <div className="flex justify-between mb-2">
                  <span className="text-xs md:text-sm font-medium text-foreground">
                    {skill.name}
                    {skill.category === 'strong' && (
                      <span className="ml-2 text-xs text-accent">(Strong)</span>
                    )}
                  </span>
                  <span className="text-xs md:text-sm text-muted-foreground">{skill.level}%</span>
                </div>
                <div className="h-2 bg-secondary rounded-full overflow-hidden">
                  <div
                    className="h-full bg-accent rounded-full transition-all duration-1000 ease-out"
                    style={{ width: `${skill.level}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Awards */}
      {data.awards.length > 0 && (
        <div>
          <div className="flex items-center gap-2 md:gap-3 mb-6">
            <Award className="w-5 h-5 md:w-6 md:h-6 text-accent" />
            <h3 className="text-xl md:text-2xl font-bold text-foreground">Awards</h3>
          </div>
          <div className="space-y-4">
            {data.awards.map((item, index) => (
              <div key={index} className="relative pl-5 md:pl-6 pb-6 border-l-2 border-border last:pb-0">
                <div className="absolute -left-[9px] top-0 w-4 h-4 rounded-full bg-accent" />
                <div className="flex items-start justify-between gap-2">
                  <h4 className="text-base md:text-lg font-semibold text-foreground mb-2">{item.title}</h4>
                  {item.link && (
                    <a
                      href={item.link}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-accent hover:text-accent/80 transition-colors flex-shrink-0"
                    >
                      <ExternalLink className="w-4 h-4" />
                    </a>
                  )}
                </div>
                <p className="text-xs md:text-sm text-accent mb-2">{item.period}</p>
                <p className="text-xs md:text-sm text-muted-foreground leading-relaxed">{item.description}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Certifications */}
      {data.certifications.length > 0 && (
        <div>
          <div className="flex items-center gap-2 md:gap-3 mb-6">
            <Medal className="w-5 h-5 md:w-6 md:h-6 text-accent" />
            <h3 className="text-xl md:text-2xl font-bold text-foreground">Certifications</h3>
          </div>
          <div className="space-y-4">
            {data.certifications.map((item, index) => (
              <div key={index} className="relative pl-5 md:pl-6 pb-6 border-l-2 border-border last:pb-0">
                <div className="absolute -left-[9px] top-0 w-4 h-4 rounded-full bg-accent" />
                <div className="flex items-start justify-between gap-2">
                  <h4 className="text-base md:text-lg font-semibold text-foreground mb-2">{item.title}</h4>
                  {item.link && (
                    <a
                      href={item.link}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-accent hover:text-accent/80 transition-colors flex-shrink-0"
                    >
                      <ExternalLink className="w-4 h-4" />
                    </a>
                  )}
                </div>
                <p className="text-xs md:text-sm text-accent mb-2">{item.period}</p>
                <p className="text-xs md:text-sm text-muted-foreground leading-relaxed">{item.organization}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Activities */}
      {data.activities.length > 0 && (
        <div>
          <div className="flex items-center gap-2 md:gap-3 mb-6">
            <Activity className="w-5 h-5 md:w-6 md:h-6 text-accent" />
            <h3 className="text-xl md:text-2xl font-bold text-foreground">Activities</h3>
          </div>
          <div className="space-y-4">
            {data.activities.map((item, index) => (
              <div key={index} className="relative pl-5 md:pl-6 pb-6 border-l-2 border-border last:pb-0">
                <div className="absolute -left-[9px] top-0 w-4 h-4 rounded-full bg-accent" />
                <div className="flex items-start justify-between gap-2">
                  <h4 className="text-base md:text-lg font-semibold text-foreground mb-2">{item.title}</h4>
                  {item.link && (
                    <a
                      href={item.link}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-accent hover:text-accent/80 transition-colors flex-shrink-0"
                    >
                      <ExternalLink className="w-4 h-4" />
                    </a>
                  )}
                </div>
                <p className="text-xs md:text-sm text-accent mb-2">{item.period}</p>
                <p className="text-xs md:text-sm text-muted-foreground leading-relaxed">{item.content}</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
