import { Mail } from 'lucide-react'
import { Github } from 'lucide-react'

interface ContactData {
  email: string
  github: string | null
}

interface ContactSectionProps {
  data: ContactData
}

export function ContactSection({ data }: ContactSectionProps) {
  return (
    <div className="space-y-6 md:space-y-8">
      <div>
        <h2 className="text-2xl md:text-3xl font-bold text-foreground mb-4">Contact</h2>
        <div className="w-10 h-1 bg-accent rounded-full mb-6" />
      </div>

      <p className="text-sm md:text-base text-muted-foreground leading-relaxed">
        Feel free to reach out via email or check out my work on GitHub.
      </p>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 md:gap-4">
        <a
          href={`mailto:${data.email}`}
          className="flex items-center gap-3 md:gap-4 p-4 md:p-5 bg-secondary rounded-xl md:rounded-2xl border border-border hover:border-accent transition-colors group"
        >
          <div className="w-12 h-12 md:w-14 md:h-14 bg-accent/10 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-accent/20 transition-colors">
            <Mail className="w-5 h-5 md:w-6 md:h-6 text-accent" />
          </div>
          <div className="min-w-0">
            <h3 className="text-xs md:text-sm font-medium text-muted-foreground mb-1">Email</h3>
            <p className="text-sm md:text-base text-foreground hover:text-accent transition-colors font-medium truncate">
              {data.email}
            </p>
          </div>
        </a>

        {data.github && (
          <a
            href={data.github}
            target="_blank"
            rel="noopener noreferrer"
            className="flex items-center gap-3 md:gap-4 p-4 md:p-5 bg-secondary rounded-xl md:rounded-2xl border border-border hover:border-accent transition-colors group"
          >
            <div className="w-12 h-12 md:w-14 md:h-14 bg-accent/10 rounded-xl flex items-center justify-center flex-shrink-0 group-hover:bg-accent/20 transition-colors">
              <Github className="w-5 h-5 md:w-6 md:h-6 text-accent" />
            </div>
            <div className="min-w-0">
              <h3 className="text-xs md:text-sm font-medium text-muted-foreground mb-1">GitHub</h3>
              <p className="text-sm md:text-base text-foreground hover:text-accent transition-colors font-medium truncate">
                {data.github.replace('https://github.com/', '')}
              </p>
            </div>
          </a>
        )}
      </div>
    </div>
  )
}
