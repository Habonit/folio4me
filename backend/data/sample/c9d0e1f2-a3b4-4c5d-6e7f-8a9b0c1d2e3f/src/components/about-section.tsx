interface AboutData {
  description: string[]
}

interface AboutSectionProps {
  data: AboutData
}

export function AboutSection({ data }: AboutSectionProps) {
  return (
    <div>
      <h2 className="text-2xl md:text-3xl font-bold text-foreground mb-4">About Me</h2>
      <div className="w-10 h-1 bg-accent rounded-full mb-6" />
      <div className="space-y-4 text-sm md:text-base text-muted-foreground leading-relaxed">
        {data.description.map((paragraph, index) => (
          <p key={index}>{paragraph}</p>
        ))}
      </div>
    </div>
  )
}
