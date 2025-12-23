import { ExternalLink } from 'lucide-react'

interface ProjectItem {
  title: string
  description: string
  tech: string[]
  link: string | null
  period: string
}

interface PortfolioData {
  projects: ProjectItem[]
}

interface PortfolioSectionProps {
  data: PortfolioData
}

export function PortfolioSection({ data }: PortfolioSectionProps) {
  return (
    <div className="space-y-6 md:space-y-8">
      <div>
        <h2 className="text-2xl md:text-3xl font-bold text-foreground mb-4">Portfolio</h2>
        <div className="w-10 h-1 bg-accent rounded-full mb-6" />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
        {data.projects.map((project, index) => (
          <div
            key={index}
            className="group bg-secondary rounded-xl md:rounded-2xl border border-border p-4 md:p-6 hover:border-accent transition-all duration-300 hover:shadow-xl hover:shadow-accent/10"
          >
            <div className="flex items-start justify-between gap-3 mb-3">
              <h3 className="text-lg md:text-xl font-bold text-foreground group-hover:text-accent transition-colors">
                {project.title}
              </h3>
              {project.link && (
                <a
                  href={project.link}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex-shrink-0 w-8 h-8 rounded-lg bg-background border border-border flex items-center justify-center text-muted-foreground hover:text-accent hover:border-accent transition-colors"
                  aria-label="Visit project"
                >
                  <ExternalLink className="w-4 h-4" />
                </a>
              )}
            </div>

            <p className="text-xs md:text-sm text-accent mb-3">{project.period}</p>

            <p className="text-xs md:text-sm text-muted-foreground leading-relaxed mb-4">
              {project.description}
            </p>

            {project.tech.length > 0 && (
              <div className="flex flex-wrap gap-2">
                {project.tech.map((tech, techIndex) => (
                  <span
                    key={techIndex}
                    className="px-2.5 py-1 bg-background border border-border rounded-lg text-xs text-muted-foreground"
                  >
                    {tech}
                  </span>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}
